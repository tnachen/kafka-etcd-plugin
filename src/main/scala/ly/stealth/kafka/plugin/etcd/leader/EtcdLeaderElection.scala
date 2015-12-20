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

import scala.collection.JavaConverters._
import scala.collection.mutable


class EtcdLeaderElection(
  config: Config,
  resourceName: String = "kafka_controller") extends LeaderElection with LogUtils {

  override def service: String = resourceName

  override def getLeader: Option[(String, String)] = {
    None
  }

  private val renewTaskLock = new Object()
  private var renewingTaskFuture: ScheduledFuture[_] = null

  val listeners = new mutable.ListBuffer[LeaderChangeListener]()

  private def tryAcquire(candidate: String, supData: String): Unit = {
    logger.info(s"Trying to acquire leadership for $candidate")
  }

  private def cancelRenewTask(candidate: String) = {
    renewTaskLock.synchronized {
      if (renewingTaskFuture != null) {
        logger.info(s"Renewing task is not empty - this candidate $candidate was a leader, cancelling renew task")
        renewingTaskFuture.cancel(true)
        renewingTaskFuture = null
      }
    }
  }

  private def setupLeaderWatchers(candidate: String, supData: String): Unit = {
  /*
    cacheListenerRegistry.addValueChangeListener(resourceName, getLeader.map(_._1), new ValueChangeListener {
      override def valueChanged(newValue: Option[String]): Unit = {
        logger.info(s"New leader value - $newValue")

        newValue match {
          case Some(newLeader) =>
            if (newLeader == candidate) {
              logger.info(s"Candidate $candidate acquired leadership, starting renewing task")
              startRenewTask(candidate, supData)
            } else {
              cancelRenewTask(candidate)
            }
          case None =>
            cancelRenewTask(candidate)
            tryAcquire(candidate, supData)
        }

        logger.info(s"Calling on leader change listeners: ${listeners.size} total")
        listeners.synchronized {
          listeners.foreach {
            l => l.onLeaderChange(newValue)
          }
        }
      }
    })
    */
  }

  override def nominate(candidate: String, supData: String) {
    setupLeaderWatchers(candidate, supData)
    tryAcquire(candidate, supData)
  }

  override def resign(leader: String): Unit = {
    //val boundStatement = new BoundStatement(deleteLeaderStmt)
    //session.execute(boundStatement.bind(resourceName, leader))
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