package meowtro.eventSystem;

import java.util.List;

import meowtro.game.City;
import meowtro.game.Game;
import meowtro.game.Region;

public class SatisfactionSettle {
    private City city;
    private double moneyWeight;
    private double spawnWeight;
    private int standardValue;
    private int standardBonus;
    private double minSpawnRate;
    public SatisfactionSettle(City city, double moneyWeight, double spawnWeight, int standardValue, int standardBonus){
        this.city = city;
        this.moneyWeight = moneyWeight;
        this.spawnWeight = spawnWeight;
        this.standardValue = standardValue;
        this.standardBonus = standardBonus;
        this.minSpawnRate = Double.parseDouble(Game.getConfig().get("spawn.rate.min"));
    }
    public void trigger() {
        List<Region> allRegions = this.city.getRegions();
        int satisfaction = this.city.getGlobalSatisfaction();
        for(Region r: allRegions){
            double curRate = r.getSpawnRate();
            double finalSpawnRate = Math.max(curRate*this.spawnWeight*(satisfaction/this.standardValue), this.minSpawnRate);
            r.setSpawnRate(finalSpawnRate);
        }
        Game.setBalance((int) Math.round(Game.getBalance() + this.standardBonus*this.moneyWeight*(satisfaction/this.standardValue)));
        Game.satisfactionBarRate = (double) satisfaction/(2*this.standardValue);
    }
}