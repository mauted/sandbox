package sandbox.world;

import java.awt.Color;

import sandbox.GameWrapper;
import sandbox.PixelBuffer;
import sandbox.StarField;

/**
 * Advances time-of-day. Lighting is applied via sprite palettes (see {@link sandbox.config.Palettes});
 * the overlay only draws stars — no full-screen dim that hides the world.
 *
 * 0 = dawn, ~0.3 = noon, 0.5 = dusk, 0.75 = midnight.
 */
public class DayNightCycle {
    /** Full day length in seconds. */
    private static final double DAY_LENGTH = 120.0;
    /** Discrete palette steps so we rebuild sprites a few times per day, not every frame. */
    private static final int LIGHTING_STEPS = 5;

    private double timeOfDay = 0.18; // start mid-morning
    private final StarField stars = new StarField(40);

    public void update(double dt) {
        timeOfDay = (timeOfDay + dt / DAY_LENGTH) % 1.0;
    }

    public double getTimeOfDay() {
        return timeOfDay;
    }

    /** 0 = full day brightness, 1 = fully night-tinted palettes. */
    public double getNightFactor() {
        if (timeOfDay >= 0.20 && timeOfDay <= 0.45) {
            return 0;
        }
        if (timeOfDay > 0.45 && timeOfDay < 0.55) {
            return (timeOfDay - 0.45) / 0.10;
        }
        if (timeOfDay >= 0.55 || timeOfDay <= 0.10) {
            return 1;
        }
        return 1.0 - (timeOfDay - 0.10) / 0.10;
    }

    /** Quantized 0 .. LIGHTING_STEPS-1 for cheap palette rebuilds. */
    public int getLightingStep() {
        return (int) Math.round(getNightFactor() * (LIGHTING_STEPS - 1));
    }

    public double getNightFactorForStep(int step) {
        return step / (double) (LIGHTING_STEPS - 1);
    }

    public boolean isNight() {
        return getNightFactor() > 0.35;
    }

    /** Stars only — world readability comes from palette shifts. */
    public void renderOverlay(PixelBuffer buffer) {
        if (getNightFactor() < 0.35) {
            return;
        }
        Color star = new Color(230, 230, 210);
        for (int i = 0; i < stars.getNumberOfStars(); i++) {
            if (((i + (int) (timeOfDay * 40)) & 3) != 0) {
                buffer.setPixel(stars.getX(i), stars.getY(i), star);
            }
        }
    }

    public String phaseLabel() {
        if (timeOfDay < 0.15) {
            return "DAWN";
        }
        if (timeOfDay < 0.45) {
            return "DAY";
        }
        if (timeOfDay < 0.55) {
            return "DUSK";
        }
        return "NIGHT";
    }
}
