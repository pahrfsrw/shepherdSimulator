package threadHelpher;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import ai.FitnessAnalyzer;
import simulation.EntityManager;
import simulation.MainLoop;
import simulation.Shepherd;
import simulation.SimulationResult;

public class MyMonitor {
	//public static Object lock = new Object();
	public static SimulationResult result = null;
	public static Shepherd shepherd = null;
	
	final static Lock lock = new ReentrantLock();
	final static Condition noShepherd  = lock.newCondition(); 
	final static Condition noResult = lock.newCondition(); 
	
	public static SimulationResult getResult(Shepherd s){
		lock.lock();
		SimulationResult res;
		shepherd = s;
		EntityManager.getInstance().setShepherd(s);
		noShepherd.signal();
		try{
			
			while(result == null)
			{
				noResult.await();
			}
			
			
		} catch (InterruptedException e){
			e.printStackTrace();
		}
		finally 
		{
			res = result;
			result = null;
			lock.unlock();
		}
		return res;
	}
	
	public static Shepherd produceResult(SimulationResult res){
		lock.lock();
		result = res;
		Shepherd s;
		try{
			noResult.signal();
			while(shepherd == null)
			{
				noShepherd.await();
			}
			
		} catch (InterruptedException e){
			e.printStackTrace();
		}
		finally
		{
			s = shepherd;
			shepherd = null;
			lock.unlock();
		}
		return s;
	}
}
