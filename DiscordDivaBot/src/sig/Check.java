package sig;

import java.util.TimerTask;

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
		if (!dead&&System.currentTimeMillis()-lastChannelMessage>=10000) {
			dead=true;
			bot.getTextChannelById(744692511703826462l).sendMessage("<:sakiDead1:799650259487293440><:sakiDead2:799650259746947083>")
			.queue();
		}
	}
	
}