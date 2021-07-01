package meowtro.game.onClickEvent;

import meowtro.Position;
import meowtro.game.Game;
import meowtro.game.entityManager.EntityManager;
import meowtro.metro_system.railway.Railway;
import meowtro.metro_system.train.Locomotive;

public class CarBuilder extends OnClickEvent {
    private EntityManager em;

    public CarBuilder(EntityManager em, Game game) {
        this.name = "car builder";
        this.game = game;
        this.em = em;
    }
    public void conduct(Locomotive locomotive) {
        em.build(locomotive);
        this.game.setNowEvent(new WaitForClick(this.game));
    }

}
