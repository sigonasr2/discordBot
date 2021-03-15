package sig;

import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import net.dv8tion.jda.api.JDA;

public class Check extends TimerTask{
	public boolean dead=false;
	public long lastChannelMessage=System.currentTimeMillis();
	public JDA bot;
	
	public Check(JDA bot) {
		this.bot=bot;
	}

	@Override
	public void run() {
		if (!dead&&System.currentTimeMillis()-lastChannelMessage>=10800000) {
			dead=true;
			bot.getTextChannelById(772923108997857291l).sendTyping().queue();
			bot.getTextChannelById(772923108997857291l).sendMessage("<:sakiDead1:799650259487293440><:sakiDead2:799650259746947083>")
			.queueAfter(1500,TimeUnit.MILLISECONDS);
			/*bot.getTextChannelById(744692511703826462l).sendMessage("<:sakiPls:820748397031718923><:sakiPls:820748397031718923>")
			.queue();*/
		}
	}
	
}