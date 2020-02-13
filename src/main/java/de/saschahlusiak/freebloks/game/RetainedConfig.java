package de.saschahlusiak.freebloks.game;

import de.saschahlusiak.freebloks.client.GameClientThread;
import de.saschahlusiak.freebloks.network.message.MessageServerStatus;
import de.saschahlusiak.freebloks.view.model.Intro;
import de.saschahlusiak.freebloks.view.model.Sounds;

class RetainedConfig {
	GameClientThread clientThread;
	MessageServerStatus lastStatus;
	Sounds soundPool;
	Intro intro;
	ConnectTask connectTask;
}