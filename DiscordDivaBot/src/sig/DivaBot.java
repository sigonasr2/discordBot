package sig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.security.auth.login.LoginException;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.message.MessageDeleteEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.EventListener;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.dv8tion.jda.core.requests.RestAction;
import sig.utils.FileUtils;

public class DivaBot extends ListenerAdapter{
	public static String token;
	public static JDA bot;
	public static HashMap<Long,Message> messageHistory = new HashMap<>();
	
	public static void main(String[] args) throws LoginException, InterruptedException {
		String[] fileContents = FileUtils.readFromFile("clientToken.txt");
		//System.out.println(fileContents[0]);
		token = fileContents[0];
		bot = new JDABuilder(token)
				.addEventListener(new DivaBot()).build();
		bot.awaitReady();
		
		List<TextChannel> channels = bot.getTextChannelsByName("bot-tests",true);
		RestAction<List<Message>> messageHistory = channels.get(0).getHistory().retrievePast(5);
		messageHistory.queue(history->
		{
			for (int i=0;i<history.size();i++) {
				System.out.println(history.get(i).getAuthor().getName()+" "+history.get(i).getAuthor().getId());
				System.out.println(history.get(i).getContentDisplay());
			}
		});
	}
	
	//https://ci.dv8tion.net/job/JDA/javadoc/net/dv8tion/jda/api/hooks/ListenerAdapter.html
	
	@Override
	public void onMessageReceived(MessageReceivedEvent ev) {
		/*System.out.println(ev.getAuthor().getName());
		System.out.println(ev.getMessage().getContentDisplay());
		System.out.println(ev.getChannel().getName());*/
		
		//System.out.println(ev.getAuthor().getIdLong());
		
		if (ValidMessage(ev.getAuthor(),ev.getChannel())) {
			ev.getChannel().sendMessage(ev.getAuthor().getName()+" typed '"+ev.getMessage().getContentDisplay()+"'!")
			.queue();
			messageHistory.put(ev.getMessageIdLong(),ev.getMessage());
		}
	}
	
	@Override
	public void onMessageDelete(MessageDeleteEvent ev) {
		if (ValidMessage(null,ev.getChannel())) {
			Long messageId = ev.getMessageIdLong();
			ev.getChannel().sendMessage("Message was deleted: "+messageHistory.get(messageId).getAuthor().getName()+": "+messageHistory.get(messageId).getContentDisplay())
			.queue();
		}
	}
	
	public boolean ValidMessage(User author,MessageChannel channel) {
		return (author==null||author.getIdLong()!=744693388632391762l)
				&&channel.getName().equalsIgnoreCase("bot-tests");
	}
}
