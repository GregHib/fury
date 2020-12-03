package com.fury.game.system.mysql.motivote;


import java.util.ArrayList;

public final class Motivote<T extends Incentive>
{
		
	private final MotivoteHandler<T> handler;
	private MotivoteThread worker;
	private final String securityKey;
	private final String pageURL;

	protected final ArrayList<Integer> finalized = new ArrayList<Integer>();
	protected final ArrayList<Integer> pending = new ArrayList<Integer>();
	
	public Motivote(MotivoteHandler<T> handler, String webDir, String securityKey)
	{
		this.handler = handler;
		this.pageURL = webDir + "databack.php";
		this.securityKey = securityKey;

	}
	
	public void start()
	{
		if (worker == null)
		{
			worker = new MotivoteThread(this);
			worker.start();
		}
	}
	
	public void terminate() {
		if(worker != null) {
			worker.setRunning(false);
			worker = null;
		}
	}
	
	public String pageURL()
	{
		return pageURL;
	}
	
	public String securityKey()
	{
		return securityKey;
	}
	
	public MotivoteHandler<T> handler()
	{
		return handler;
	}
	
	public void fail(T incentive)
	{
		synchronized(pending)
		{
			synchronized(finalized)
			{
				pending.remove((Integer)incentive.internalID());
			}
		}
	}
	
	public void complete(T incentive)
	{
		synchronized(pending)
		{
			synchronized(finalized)
			{
				pending.remove((Integer)incentive.internalID());
				finalized.add(incentive.internalID());
			}
		}
		
		System.out.println("Finalizing " + incentive);
	}
	
	public static void print(Object string)
	{
		System.out.println("[MOTIVOTE] " + string);
	}
}
