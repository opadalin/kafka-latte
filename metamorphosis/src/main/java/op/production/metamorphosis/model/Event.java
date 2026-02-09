package op.production.metamorphosis.model;

import java.time.Instant;

public record Event(String id, String source, String type, String payload, Instant timestamp) {}
