package edu.wm.cs.cs301.IgnatMiagkov.generation;

import java.util.ArrayList;
import java.util.Random;

import edu.wm.cs.cs301.IgnatMiagkov.gui.Constants;
/**
 * Maze generation via Boruvka Algorithm
 * @author Ignat Miagkov
 * 
 */
public class MazeBuilderBoruvka extends MazeBuilder {
	
	/**
	 * 
	 * Component is used to store each of the different components of the "graph" as components are connected
	 * Each component has id (to compare the components) and a list of boards to be removed for that component
	 */
	private class Component{
		int id;
		ArrayList<Wallboard> boards;
		
		/**
		 *Generates a new component for the unique ID. Initializes new ArrayList of Wallboard
		 * @param ID
		 */
		Component(int ID){
			this.id = ID;
			boards = new ArrayList<Wallboard>();
		}
		
		/** Get ID of specific component
		 * @return ID
		 */
		private int getId() {
			return this.id;
		}
		
		/**
		 * @return list of wallboards for component
		 */
		private ArrayList<Wallboard> getBoards(){
			return this.boards;
		}
		
		/** Merges all candidate boards of another component into this current component
		 * @param c
		 */
		private void Merge(Component c) {
			this.boards.addAll(c.getBoards());
		}
		
	}
	public MazeBuilderBoruvka() {
		super();
	}
	
	
	
	/**Wallboard is assigned a pseudorandom value based on its position in the board. 
	 * Wallboards that are the same but assigned from different cells will return the same edge weight.
	 * @param w
	 * @return Random edge weight for specific wallboard. Equivalent wallboards return the same random edge weight.
	 */
	public int getEdgeWeight(Wallboard w) {
		Random rnd = new Random();
		int combo = (4 * width * w.getY()) + (4 * w.getX());
		switch(w.getDirection()) {
		case North:
			combo -= (4 * width);
			break;
		case East:
			combo += 1;
			break;
		case West:
			combo -= 3;
			break;
		default:
			combo += 0;
			break;
		}
		rnd.setSeed(combo);
		return rnd.nextInt(100000);
	}
	
	
	/** First, make new 2D matrix for maze representation with component objects to represent all of them.
	 * Iterate through each cycle while number of components is greater than one
	 * Each cycle, update list of possible wallboards for that component, then grab smallest one that goes to different component.
	 * Merge components across the newly torn down wallboard. Number of components in comp list gets smaller.
	 * Repeat until only one component exists.
	 */
	@Override
	protected void generatePathways() {
		// so the plan is to iterate through all the cells first and choose the cheapest edge for all of them
		// that can be torn down
		
		// initialize a list of all cells in the maze called components (a list of cell lists)
//		ArrayList<ArrayList<Cell>> components = new ArrayList<ArrayList<Cell>>();
//		for (int i = 0; i < width; i++) {
//			for (int j = 0; j < height; j++) {
//				Cell w = new Cell(i, j);
//				ArrayList<Cell> new_comp= new ArrayList<Cell>();
//				new_comp.add(w);
//				components.add(new_comp);
//			}
//		}
		
		int [][] cells = new int[width][height];
		
		int count = 0;
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				cells[i][j] = count;
				count++;
			}
		}
		
		count = 0; 
		ArrayList<Component> components = new ArrayList<Component>();
		for (int i = 0; i < width * height; i++) {
			components.add(new Component(i));
		}
		// for the first round, go through every cell and find the cheapest edge for each cell.
		// If that edge has not already been declared for destruction, add it to list of wallboard to be killed
		// final ArrayList<Wallboard> candidates = new ArrayList<Wallboard>();
		// also make a list of boards to be destroyed 
		// 
		
		while(!checkForOneComponent(cells)) {
			for (int k = 0; k < components.size(); k++) {
				for (int i = 0; i < width; i++) {
					for (int j = 0; j < height; j++) {
						if(cells[i][j] == components.get(k).getId())
							updateListOfWallboards(i, j, components.get(k).getBoards());
					}
				}
				Wallboard curWallboard = getSmallestEdgeWeight(cells, components.get(k).getBoards());
				if (floorplan.canTearDown(curWallboard) && cells[curWallboard.getNeighborX()][curWallboard.getNeighborY()] != components.get(k).getId()) {
					int neighbor = cells[curWallboard.getNeighborX()][curWallboard.getNeighborY()];
					components.get(k).Merge(getComp(components, neighbor));
					components.remove(getComp(components, neighbor));
					merge(cells, cells[curWallboard.getX()][curWallboard.getY()], neighbor);
					floorplan.deleteWallboard(curWallboard);
				}
			}
			
		}
//			for (ArrayList<Cell> comp: components) {
//				updateListOfWallboards(comp, candidates);
//				if (!toDestroy.contains(getSmallestEdgeWeight(candidates)))
//					toDestroy.add(getSmallestEdgeWeight(candidates));
//				
//			}
//			for (Wallboard wall: toDestroy) {
//				merge(wall, components);
//				floorplan.deleteWallboard(wall);
//			}
//			
//			toDestroy.clear();
//			candidates.clear();
			
	}
		// maybe here have a while loop for while components > 1
		// for (components : floor plan)
		// 		returnPossibleWallboardsForDestruction(components, candidates) (I plan on stealing this from prim)
		//		get the one with largest edge weight (get all edge weight and choose smallest one)
		//		add to candidates list for destruction , or mark for destruction (add to list to be destoyed)
		//		if already in list to be destroyed:
		//			dont add
		//		for each wallboard to be destroyed:
		//			merge components get x, y and components get_neighbor x and y
		//			
		//
		// 
	
	/** 
	 * @param comp list
	 * @param ID desired
	 * @return the component for the specific ID outlined in the 2D matrix of our maze cells
	 */
	private Component getComp(ArrayList<Component> comp, int ID) {
		for (Component parts: comp) {
			if (parts.getId() == ID) {
				return parts;
			}
		}
		return null;
	}
	
	/**
	 * @param cells 2D array
	 * @return true/false based on how many components still exist
	 */
	private boolean checkForOneComponent(int[][] cells) {
		int curr = cells[0][0];
		for (int i = 0; i < cells.length; i++) {
			for (int j = 0; j < cells[0].length; j++) {
				if (cells[i][j] != curr)
					return false;
			}
		}
		return true;
	}
	
	/** Merges the different "components" on the 2D array. Given x and y, all cells that are equal to y become x.
	 * @param cells
	 * @param x
	 * @param y
	 */
	private void merge(int[][] cells, int x, int y) {
		for (int i = 0; i < cells.length; i++) {
			for (int j = 0; j < cells[0].length; j++) {
				if (cells[i][j] == y)
					cells[i][j] = x;
			}
		}
	}
	
	/**Updates list of possible wallboards to tear down for the given component (or in this case the given cell).
	 * This methods is used repeatedly with each cell in that component to create a full list of possible wallboards for that component.
	 * @param x position
	 * @param y position
	 * @param wallboards 
	 */
	private void updateListOfWallboards(int x, int y , ArrayList<Wallboard> wallboards) {
		Wallboard wallboard = new Wallboard(x, y, CardinalDirection.East) ;
		for (CardinalDirection cd : CardinalDirection.values()) {
			wallboard.setLocationDirection(x, y, cd);
			if (floorplan.canTearDown(wallboard)) 
			{
				wallboards.add(new Wallboard(x, y, cd));
			}
		}
	}
	
	/**First, finds any edge that connects to a different component (first for loop).
	 * Then, finds the smallest of the edges that do connect to a different component. If no smaller one is found, original is returned.
	 * @param cells
	 * @param candidates Wallboards
	 * @return wallboard to be torn down 
	 */
	private Wallboard getSmallestEdgeWeight(int[][] cells, ArrayList<Wallboard> candidates) {
		int index = 0;
		for (int i = 0; i < candidates.size(); i++) {
			if(cells[candidates.get(i).getNeighborX()][candidates.get(i).getNeighborY()] != cells[candidates.get(i).getX()][candidates.get(i).getY()]) {
				index = i;
			}
		}
		for (int i = 0; i < candidates.size(); i++) {
			if (getEdgeWeight(candidates.get(i)) <= getEdgeWeight(candidates.get(index)) && cells[candidates.get(i).getNeighborX()][candidates.get(i).getNeighborY()] != cells[candidates.get(i).getX()][candidates.get(i).getY()]) {
				index = i;
			}
		}
		return candidates.remove(index);
	}				

}
