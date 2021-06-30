package meowtro.game.onClickEvent;

import meowtro.Position;
import meowtro.game.Game;
import meowtro.game.entityManager.EntityManager;
import meowtro.metro_system.railway.Railway;

public class LocomotiveBuilder extends OnClickEvent {
    private EntityManager em;

    public LocomotiveBuilder(EntityManager em, Game game) {
        this.name = "locomotive builder";
        this.game = game;
        this.em = em;
    }
    public void conduct(Railway railway, Position position) {
        em.build(railway, position);
        this.game.setNowEvent(new WaitForClick(this.game));
    }

}
