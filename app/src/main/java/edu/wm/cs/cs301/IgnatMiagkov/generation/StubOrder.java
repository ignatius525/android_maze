package edu.wm.cs.cs301.IgnatMiagkov.generation;

import java.util.Random;

public class StubOrder implements Order {
	
	private int skillLevel;
	private int seed;
	private Builder builder;
	private boolean perfect;
	private Maze maze;
	
	public StubOrder() {
		
	}
	
	public StubOrder(String builder, int level, boolean perfect, int seed) {
		this.setBuilder(builder);
		this.setSkillLevel(level);
		this.setIsPerfect(perfect);
		this.seed = seed;
	}
	public StubOrder(String builder, int level, boolean perfect) {
		this.setBuilder(builder);
		this.setSkillLevel(level);
		this.setIsPerfect(perfect);
		Random rnd = new Random();
		this.seed = rnd.nextInt();
	}
	
	@Override
	public int getSkillLevel() {
		// TODO Auto-generated method stub
		return skillLevel;
	}

	@Override
	public Builder getBuilder() {
		// TODO Auto-generated method stub
		return builder;
	}

	@Override
	public boolean isPerfect() {
		// TODO Auto-generated method stub
		return perfect;
	}

	@Override
	public int getSeed() {
		// TODO Auto-generated method stub
		return seed;
	}
	
	public Maze getMaze() {
		return maze;
	}
	
	public void setBuilder(String builder) {
		switch (builder) {
		case "Eller":
			this.builder = Order.Builder.Eller;
		case "Prim":
			this.builder = Order.Builder.Prim;
		case "Kruskal":
			this.builder = Order.Builder.Kruskal;
		case "Boruvka":
			this.builder = Order.Builder.Boruvka;
		default:
			this.builder = Order.Builder.DFS;
		}
		
	}
	
	public void setSkillLevel(int level) {
		this.skillLevel = level;
	}
	
	@Override
	public void deliver(Maze mazeConfig) {
		// TODO Auto-generated method stub
		maze = mazeConfig;
	}
	
	public void setIsPerfect(boolean perfect) {
		this.perfect = perfect;
	}

	@Override
	public void updateProgress(int percentage) {
		// TODO Auto-generated method stub

	}

}
