package newhorizon.expand.eventsys;

import arc.util.TaskQueue;
import newhorizon.expand.entities.WorldEvent;

import static mindustry.Vars.net;
import static mindustry.Vars.state;

public class EventHandler implements Runnable{
	private static EventHandler eventHandler;
	public static EventHandler get(){return eventHandler;}
	public static void create(){
		eventHandler = new EventHandler();
		eventHandler.start();
	}
	
	public static void dispose(){
		if(eventHandler == null)return;
		eventHandler.stop();
		eventHandler.queue.clear();
		eventHandler = null;
	}
	
	private Thread thread;
	
	private static final int updateFPS = 60;
	private static final int updateInterval = 1000 / updateFPS;
	
	private final TaskQueue queue = new TaskQueue();
	
	public static boolean inValidEvent(WorldEventType event){
		return event == null || event == WorldEventType.NULL;
	}
	
	public static void runEventOnce(String tag, Runnable runnable){
		if(!state.rules.tags.containsKey(tag)){
			runnable.run();
			state.rules.tags.put(tag, "true");
		}
	}
	
	public EventHandler(){
	}
	
	public void posCalculation(WorldEvent event){
		post(() -> event.set(event.type.target(event)));
	}
	
	public void post(Runnable runnable){
		queue.post(runnable);
	}
	
	private void start(){
		stop();
		if(net.client())return;
		
		thread = new Thread(this, "EventHandler");
		thread.setPriority(Thread.MIN_PRIORITY);
		thread.setDaemon(true);
		thread.start();
	}
	
	private void stop(){
		if(thread != null){
			thread.interrupt();
			thread = null;
		}
		queue.clear();
	}
	
	@Override
	public void run(){
		while(true){
			if(net.client()) return;
			try{
				
				if(state.isGame()){
					queue.run();
				}
				
				try{
					//noinspection BusyWait
					Thread.sleep(updateInterval);
				}catch(InterruptedException e){
					//stop looping when interrupted externally
					return;
				}
			}catch(Throwable e){
				e.printStackTrace();
			}
		}
	}
}
