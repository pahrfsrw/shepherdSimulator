package threadHelpher;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

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
		if(s != null)
			shepherd = s.clone();
		else
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
			res = result.clone(); // Mikilv�gt a� �a� s� clone h�r! Sj� comment vi� shepherd.clone() h�r a� ne�an.
			result = null;
			lock.unlock();
		}
		return res;
	}
	
	public static Shepherd produceResult(SimulationResult res){
		lock.lock();
		if(res != null)
			result = res.clone();
		else
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
			// Einhverra hluta vegna lagast corruption � uppl�singum sem �g sendi � milli ef
			// �g �tb� clone a�fer� og skila clone. �g veit ekki af hverju.
			// Corruptioni� f�lst � �v� a� hnitin � Shepherd sem var skila� voru �ll � rugli og eflaust ger�ist eitthva� meira.
			s = shepherd.clone();
			shepherd = null;
			lock.unlock();
		}
		return s;
	}
}
