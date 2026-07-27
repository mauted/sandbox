package sandbox.particles;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import sandbox.Camera;
import sandbox.PixelBuffer;
import sandbox.sprites.Sprite;
import sandbox.sprites.SpriteLibrary;

public class ParticleSystem {
    private final List<Particle> particles = new ArrayList<>();

    public void burst(double x, double y, int count, Sprite sprite) {
        for (int i = 0; i < count; i++) {
            double angle = Math.random() * Math.PI * 2;
            double speed = 0.4 + Math.random() * 1.2;
            particles.add(new Particle(
                sprite,
                x,
                y,
                Math.cos(angle) * speed,
                Math.sin(angle) * speed,
                0.3 + Math.random() * 0.35
            ));
        }
    }

    public void hitSpark(double x, double y) {
        burst(x, y, 6, SpriteLibrary.PARTICLE_SPARK.getSprite());
    }

    public void leafPuff(double x, double y) {
        burst(x, y, 5, SpriteLibrary.PARTICLE_LEAF.getSprite());
    }

    /**
     * Small splash / wake behind a moving swimmer. {@code backDx/backDy} should point
     * opposite the movement direction (unit-ish).
     */
    public void waterWake(double x, double y, double backDx, double backDy) {
        Sprite splash = SpriteLibrary.PARTICLE_WATER.getSprite();
        for (int i = 0; i < 2; i++) {
            double jitter = (Math.random() - 0.5) * 0.8;
            double px = -backDy * jitter;
            double py = backDx * jitter;
            particles.add(new Particle(
                splash,
                x + px * 4,
                y + py * 4,
                backDx * (0.3 + Math.random() * 0.5) + px * 0.4,
                backDy * (0.3 + Math.random() * 0.5) + py * 0.4,
                0.18 + Math.random() * 0.2
            ));
        }
    }

    public void update(double dt) {
        Iterator<Particle> it = particles.iterator();
        while (it.hasNext()) {
            if (!it.next().update(dt)) {
                it.remove();
            }
        }
    }

    public void render(PixelBuffer buffer, Camera camera) {
        for (Particle particle : particles) {
            particle.render(buffer, camera);
        }
    }
}
