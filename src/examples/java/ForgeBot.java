import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.user.UserOnlineStatusUpdateEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

/**
 * Created by kylekovalchick on 1/16/17.
 */
public class ForgeBot extends ListenerAdapter {


    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {

        event.getGuild().getController().addRolesToMember(event.getMember(),event.getGuild().getRoles().get(2)).queue();

    }


    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.isFromType(ChannelType.TEXT))         //If this message was sent to a Guild TextChannel
        {

            event.getGuild().getController().addRolesToMember(event.getMember(),event.getGuild().getRoles().get(2)).queue();

        }
        if (event.getMessage().getContent().equals("!ping"))
        {
            event.getChannel().sendMessage("pong!").queue();
        }
        if (event.getMessage().getContent().equals("!move"))
        {
            event.getGuild().getController().moveVoiceMember(event.getMember(),event.getGuild().getVoiceChannels().get(1)).queue();
        }
    }
}
