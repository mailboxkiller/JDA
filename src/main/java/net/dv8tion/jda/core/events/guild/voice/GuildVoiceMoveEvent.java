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

package net.dv8tion.jda.core.events.guild.voice;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.VoiceChannel;

public class GuildVoiceMoveEvent extends GenericGuildVoiceEvent
{
    protected final VoiceChannel channelLeft;
    protected final VoiceChannel channelJoined;

    public GuildVoiceMoveEvent(JDA api, long responseNumber, Member member, VoiceChannel channelLeft)
    {
        super(api, responseNumber, member);
        this.channelLeft = channelLeft;
        this.channelJoined = member.getVoiceState().getChannel();
    }

    public VoiceChannel getChannelLeft()
    {
        return channelLeft;
    }

    public VoiceChannel getChannelJoined()
    {
        return channelJoined;
    }
}
