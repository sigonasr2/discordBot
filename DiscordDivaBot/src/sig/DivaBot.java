package sig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import javax.security.auth.login.LoginException;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.RestAction;
import sig.utils.FileUtils;

public class DivaBot extends ListenerAdapter{
	public static String token;
	public static JDA bot;
	public static HashMap<Long,Message> messageHistory = new HashMap<>();
	
	public static void main(String[] args) throws LoginException, InterruptedException {
		String[] fileContents = FileUtils.readFromFile("clientToken.txt");
		//System.out.println(fileContents[0]);
		token = fileContents[0];
		JDA bot = JDABuilder.createDefault(token).build();
		bot.addEventListener(new DivaBot());
		bot.awaitReady();
		DivaBot.bot=bot;
	}
	
	//https://ci.dv8tion.net/job/JDA/javadoc/net/dv8tion/jda/api/hooks/ListenerAdapter.html
	
	@Override
	public void onMessageReceived(MessageReceivedEvent ev) {
		/*System.out.println(ev.getAuthor().getName());
		System.out.println(ev.getMessage().getContentDisplay());
		System.out.println(ev.getChannel().getName());*/
		
		//System.out.println(ev.getAuthor().getIdLong());
		
		if (ValidMessage(ev.getAuthor(),ev.getChannel(),ev.getMessage().getContentDisplay())) {
			/*ev.getChannel().sendMessage(ev.getAuthor().getName()+" typed '"+ev.getMessage().getContentDisplay()+"'!")
			.queue();*/
			//System.out.println(bot.getEmotes());
			ev.getChannel().addReactionById(ev.getMessageIdLong(), ChooseRandomMuniEmote(ev.getMessage().getContentDisplay().getBytes().length+
					ev.getAuthor().getIdLong()))
			.queue();
			//messageHistory.put(ev.getMessageIdLong(),ev.getMessage());
		}
	}
	
	private Emote ChooseRandomMuniEmote(long seed) {
		List<Emote> emotes = bot.getEmotes();
		List<Emote> muniEmotes = new ArrayList<Emote>();
		for (Emote e : emotes) {
			if (e.getName().toLowerCase().contains("muni")) {
				muniEmotes.add(e);
			}
		}
		Random r = new Random();
		return muniEmotes.get((int)(Math.random()*muniEmotes.size()));
	}

	/*@Override
	public void onMessageDelete(MessageDeleteEvent ev) {
		if (ValidMessage(null,ev.getChannel())) {
			Long messageId = ev.getMessageIdLong();
			ev.getChannel().sendMessage("Message was deleted: "+messageHistory.get(messageId).getAuthor().getName()+": "+messageHistory.get(messageId).getContentDisplay())
			.queue();
		}
	}*/
	
	public boolean ValidMessage(User author,MessageChannel channel,String message) {
		return (author==null||author.getIdLong()!=809417111859888168l)
				&&(channel.getName().equalsIgnoreCase("bot-tests")||
						channel.getIdLong()==772923108997857291l/*D4DJcord tiering channel*/||
						channel.getName().equalsIgnoreCase(author.getName()))
				&&(ContainsMoreThanJustEmote(message) && (message.toLowerCase().contains("muni")||
						message.toLowerCase().contains("むに")||
						message.toLowerCase().contains("무니")));
	}

	private boolean ContainsMoreThanJustEmote(String message) {
		int colonCount=0;
		for (int i=0;i<message.length();i++) {
			if ((colonCount==0||colonCount>=2)&&message.charAt(i)!=':') {
				return true;
			}
			if (message.charAt(i)==':') {
				colonCount++;
			}
		}
		return colonCount!=2;
	}
}
