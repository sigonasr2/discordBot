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
	public int lastMessageCount = 0;
	public String lastMessage = "";
	public String[] keywordsList= new String[]{
			"Apple","Apricot","Avocado","Banana","Bilberry","Blackberry","Blueberry","Currant","Cherry","Cherimoya","Clementine","Date","Damson","Fruit","Durian","Eggplant","Elderberry","Feijoa","Gooseberry","Grape","Grapefruit","Guava","Huckleberry","Jackfruit","Jambul","Kiwi","Kumquat","Legume","Lemon","Lime","Lychee","Mango","Mangostine","Melon","Cantaloupe","Cantalope","Honeydew","Watermelon","Rock","Nectarine","Orange","Peach","Pear","Williams","Bartlett","Pitaya","Physalis","Plum","prune","Pineapple","Pomegranate","Pomegranite","Raisin","Raspberry","blackcap","Rambutan","Redcurrant","Salal","Satsuma","Star","Strawberry","Tangerine","Tomato","Ugli","Watermelon","Ziziphus","mauritiana","Red","Orange","Yellow","Green","Blue","Purple","Pink","Brown","Gray","Grey","Black","White","Color","Dragon","Wyvern","Quetzalcoatl","Hydra","Cockatrice","Wyrm","Drake"
		};
	
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
		
		if (ApprovedChannel(ev.getChannel(),ev.getAuthor())/*&&ev.getMessage().getContentDisplay().toLowerCase().contains("muni")*/) {
			if (lastMessageCount>0&&ev.getMessage().getContentDisplay().toLowerCase().equalsIgnoreCase(lastMessage)) {
				lastMessageCount++;
			} else {
				lastMessageCount=1;
				lastMessage=ev.getMessage().getContentDisplay();
			}
			if (lastMessageCount==2) {
				ev.getChannel().sendMessage(ev.getMessage())
				.queue();
			}
		}
		
		if (ValidMessage(ev.getAuthor(),ev.getChannel(),ev.getMessage().getContentDisplay())) {
			/*ev.getChannel().sendMessage(ev.getAuthor().getName()+" typed '"+ev.getMessage().getContentDisplay()+"'!")
			.queue();*/
			//System.out.println(bot.getEmotes());
			ev.getChannel().addReactionById(ev.getMessageIdLong(), ChooseRandomMuniEmote(ev.getMessage().getContentDisplay().hashCode()+
					ev.getAuthor().getIdLong()))
			.queue();
			//messageHistory.put(ev.getMessageIdLong(),ev.getMessage());
		}
	}
	
	private Emote ChooseRandomMuniEmote(long seed) {
		List<Emote> emotes = bot.getEmotes();
		List<Emote> muniEmotes = new ArrayList<Emote>();
		for (Emote e : emotes) {
			if (e.getName().toLowerCase().contains("saki")&&
					!e.getName().toLowerCase().contains("dead1")&&
					!e.getName().toLowerCase().contains("dead2")) {
				muniEmotes.add(e);
			}
		}
		Random r = new Random(seed);
		return muniEmotes.get((int)(r.nextDouble()*muniEmotes.size()));
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
		return (author==null||author.getIdLong()!=809417111859888168l||author.getIdLong()!=820742054002294784l)
				&&(ApprovedChannel(channel,author))
				&&(ContainsMoreThanJustEmote(message) && (
						containsKeyword(message)));
	}

	private boolean containsKeyword(String message) {
		for (String s : keywordsList) {
			if (message.toLowerCase().contains(s.toLowerCase())) {
				return true;
			}
		}
		return false;
	}

	private boolean ApprovedChannel(MessageChannel channel,User author) {
		return channel.getName().equalsIgnoreCase("bot-tests")||
				channel.getIdLong()==772923108997857291l/*D4DJcord tiering channel*/||
				channel.getName().equalsIgnoreCase(author.getName());
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
