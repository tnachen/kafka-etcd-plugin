/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ly.stealth.kafka.plugin.etcd.leader.election

import ly.stealth.kafka.plugin.etcd.config.Config
import ly.stealth.kafka.plugin.etcd.log.LogUtils

import java.util.concurrent.{ScheduledFuture, ScheduledThreadPoolExecutor, TimeUnit}

import org.apache.kafka.plugin.interface.{LeaderChangeListener, LeaderElection, ValueChangeListener}

import mousio.etcd4j._
import mousio.etcd4j.responses._
import mousio.etcd4j.promises._

import scala.collection.JavaConverters._
import scala.collection.mutable

import java.util.concurrent.TimeoutException

class EtcdLeaderElection(
  config: Config,
  etcdClient: EtcdClient,
  resourceName: String = "kafka_controller") extends LeaderElection with LogUtils {

  override def service: String = resourceName

  override def getLeader: Option[(String, String)] = {
    val promise = etcdClient.get(resourceName).timeout(1, TimeUnit.SECONDS).send()
    try {
      val node = promise.get().node;
      val value = node.value;
      Option((value, null))
    } catch {
      case e: TimeoutException => {
        warn("Failed to get leader, error: " + e)
        None
      }
    }
  }

  private val listeners = new mutable.ListBuffer[LeaderChangeListener]()

  override def nominate(candidate: String, supData: String): Unit = {
    //val promise = etcdClient.get(resourceName).send()
  }

  override def resign(leader: String): Unit = {
  }

  override def addListener(listener: LeaderChangeListener) = {
    listeners.synchronized {
      listeners += listener
    }
  }

  override def removeListener(listener: LeaderChangeListener) = {
    listeners.synchronized {
      listeners -= listener
    }
  }

  override def init(context: Any): Unit = {

  }

  override def close(): Unit = {
  }
}
