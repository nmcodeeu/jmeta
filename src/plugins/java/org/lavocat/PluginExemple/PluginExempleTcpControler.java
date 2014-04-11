package org.lavocat.PluginExemple;

import java.util.HashMap;

import org.lavocat.PluginExemple.tcp.commands.ExempleCommand;
import org.meta.plugin.PluginTCPControler;
import org.meta.plugin.tcp.Command;

public class PluginExempleTcpControler extends PluginTCPControler {

	@Override
	protected void registercommands(HashMap<String, Class<? extends Command>> commands) {
		commands.put("toto", ExempleCommand.class);
	}

	@Override
	public void nodesFounded() {
		
	}

}
