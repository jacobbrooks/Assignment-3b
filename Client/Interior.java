public class Interior extends JTree{
	
	private final JTree[] quads;
	
	public Interior(JTree q1, JTree q2, JTree q3, JTree q4) {
		 quads = new JTree[] { q1, q2, q3, q4 };
	}

	@Override
	public void compute() {
		invokeAll(quads); 
	    for (int i = 0; i < quads.length; ++i) {
	      quads[i].reinitialize(); 
	    }
	}

}
