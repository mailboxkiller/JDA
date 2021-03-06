/*
 *     Copyright 2015-2016 Austin Keener & Michael Ritter
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.dv8tion.jda.client.handle;

import net.dv8tion.jda.client.entities.CallableChannel;
import net.dv8tion.jda.client.entities.Group;
import net.dv8tion.jda.client.entities.impl.CallImpl;
import net.dv8tion.jda.client.entities.impl.GroupImpl;
import net.dv8tion.jda.client.entities.impl.JDAClientImpl;
import net.dv8tion.jda.client.events.call.CallDeleteEvent;
import net.dv8tion.jda.core.entities.impl.JDAImpl;
import net.dv8tion.jda.core.entities.impl.PrivateChannelImpl;
import net.dv8tion.jda.core.handle.EventCache;
import net.dv8tion.jda.core.handle.SocketHandler;
import org.json.JSONObject;

public class CallDeleteHandler extends SocketHandler
{
    public CallDeleteHandler(JDAImpl api)
    {
        super(api);
    }

    @Override
    protected String handleInternally(JSONObject content)
    {
        String channelId = content.getString("channel_id");
        CallableChannel channel = api.asClient().getGroupById(channelId);
        if (channel == null)
            channel = api.getPrivateChannelMap().get(channelId);
        if (channel == null)
        {
            EventCache.get(api).cache(EventCache.Type.CHANNEL, channelId, () -> handle(responseNumber, allContent));
            EventCache.LOG.debug("Received CALL_DELETE for a Group/PrivateChannel that is not yet cached. JSON: " + content);
            return null;
        }

        CallImpl call = (CallImpl) channel.getCurrentCall();
        if (call == null)
        {
            EventCache.get(api).cache(EventCache.Type.CALL, channelId, () -> handle(responseNumber, allContent));
            EventCache.LOG.debug("Received a CALL_DELETE for a Call that is not yet cached. JSON: " + content);
            return null;
        }

        if (channel instanceof Group)
        {
            GroupImpl group = (GroupImpl) channel;
            group.setCurrentCall(null);
            call.getCallUserMap().forEach((userId, cUser) ->
            {
                ((JDAClientImpl) api.asClient()).getCallUserMap().remove(userId);
            });
        }
        else
        {
            PrivateChannelImpl priv = (PrivateChannelImpl) channel;
            priv.setCurrentCall(null);
            ((JDAClientImpl) api.asClient()).getCallUserMap().remove(priv.getUser().getId());
            ((JDAClientImpl) api.asClient()).getCallUserMap().remove(api.getSelfUser().getId());
        }

        api.getEventManager().handle(
                new CallDeleteEvent(
                        api, responseNumber,
                        call));
        return null;
    }
}
