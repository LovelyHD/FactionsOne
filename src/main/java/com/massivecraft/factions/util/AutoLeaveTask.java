package com.massivecraft.factions.util;

import com.massivecraft.factions.Conf;
import com.massivecraft.factions.P;

public class AutoLeaveTask implements Runnable {
	private static AutoLeaveProcessTask task;
	double rate;
	
	public AutoLeaveTask() {
		rate = Conf.autoLeaveRoutineRunsEveryXMinutes;
	}
	
	@Override
	public synchronized void run() {
		if (task != null && !task.isFinished()) {
			return;
		}
		
		task = new AutoLeaveProcessTask();
		task.runTaskTimer(P.p, 1, 1);
		
		// maybe setting has been changed? if so, restart this task at new rate
		if (rate != Conf.autoLeaveRoutineRunsEveryXMinutes) {
			P.p.startAutoLeaveTask(true);
		}
	}
}
