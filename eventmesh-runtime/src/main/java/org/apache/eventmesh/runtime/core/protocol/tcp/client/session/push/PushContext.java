/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.eventmesh.runtime.core.protocol.tcp.client.session.push;

import org.apache.eventmesh.runtime.constants.EventMeshConstants;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;


import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PushContext {

    private final SessionPusher sessionPusher;

    public final AtomicLong deliveredMsgsCount = new AtomicLong(0);

    public final AtomicLong deliverFailMsgsCount = new AtomicLong(0);

    private final ConcurrentHashMap<String /* seq */, DownStreamMsgContext> unAckMsg = new ConcurrentHashMap<>();

    private final long createTime = System.currentTimeMillis();

    public PushContext(SessionPusher sessionPusher) {
        this.sessionPusher = sessionPusher;
    }

    public void deliveredMsgCount() {
        deliveredMsgsCount.incrementAndGet();
    }

    public void deliverFailMsgCount() {
        deliverFailMsgsCount.incrementAndGet();
    }

    public void unAckMsg(String seq, DownStreamMsgContext downStreamMsgContext) {
        unAckMsg.put(seq, downStreamMsgContext);
        log.info("put msg in unAckMsg,seq:{},unAckMsgSize:{}", seq, getTotalUnackMsgs());
    }

    public int getTotalUnackMsgs() {
        return unAckMsg.size();
    }


    public ConcurrentHashMap<String, DownStreamMsgContext> getUnAckMsg() {
        return unAckMsg;
    }

    @Override
    public String toString() {
        return "PushContext{"
            +
            "deliveredMsgsCount=" + deliveredMsgsCount.longValue()
            +
            ",deliverFailCount=" + deliverFailMsgsCount.longValue()
            +
            ",unAckMsg=" + CollectionUtils.size(unAckMsg)
            +
            ",createTime=" + DateFormatUtils.format(createTime, EventMeshConstants.DATE_FORMAT) + '}';
    }
}
