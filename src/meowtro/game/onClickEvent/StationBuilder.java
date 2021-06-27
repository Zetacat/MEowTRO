package meowtro.game.onClickEvent;

import meowtro.Position;
import meowtro.game.City;
import meowtro.game.Game;
import meowtro.game.entityManager.EntityManager;

public class StationBuilder extends OnClickEvent {
    private EntityManager em;
    private City city;
    public StationBuilder(EntityManager em, Game game) {
        this.name = "station builder";
        this.game = game;
        this.city = this.game.getCity();
        this.em = em;
    }
    public void conduct(Position position) {
        em.build(city, position);
        this.game.setNowEvent(new WaitForClick(this.game));
    }
}
