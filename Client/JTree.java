import java.util.concurrent.RecursiveAction;

public abstract class JTree extends RecursiveAction{
	
	public volatile double maxDiff;
	
	public abstract void compute();
	
}
