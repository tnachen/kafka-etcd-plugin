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
package ly.stealth.kafka.plugin.etcd.listener.registry

import ly.stealth.kafka.plugin.etcd.config.Config
import org.apache.kafka.plugin.interface.{KeySetChangeListener, ValueChangeListener, ListenerRegistry}

import scala.collection.JavaConverters._

class EtcdListenerRegistry(config: Config) extends ListenerRegistry {

  /**
   * Register permanent callback for data change event
   * @param key the listenable data identifier
   * @param eventListener see [[ValueChangeListener]]
   */
  override def addValueChangeListener(key: String, eventListener: ValueChangeListener): Unit = {
    def fetcher = {
    }

    addValueChangeListener(key, eventListener)
  }

  /**
   * Register permanent callback for key-set change event
   * @param namespace the listenable key-set identifier (e.g. parent path in Zookeeper, table name in Database etc)
   * @param eventListener see [[KeySetChangeListener]]
   */
  override def addKeySetChangeListener(namespace: String, eventListener: KeySetChangeListener): Unit = {
    def fetcher = {
    }

    addKeySetChangeListener(namespace, eventListener)
  }

  /**
   * Setup everything needed for concrete implementation
   * @param context TBD. Should be abstract enough to be used by different implementations and
   *                at the same time specific because will be uniformly called from the Kafka code,
   *                regardless of the implementation
   */
  override def init(context: Any): Unit = {}

  /**
   * Release all acquired resources
   */
  override def close(): Unit = {}

  def removeKeySetChangeListener(namespace: String,eventListener: org.apache.kafka.plugin.interface.KeySetChangeListener): Unit = {}
  def removeValueChangeListener(key: String,eventListener: org.apache.kafka.plugin.interface.ValueChangeListener): Unit = {}
}