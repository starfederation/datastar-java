package starfederation.datastar.events;

import static starfederation.datastar.Consts.DEFAULT_ELEMENTS_USE_VIEW_TRANSITIONS;
import static starfederation.datastar.Consts.DEFAULT_ELEMENT_PATCH_MODE;
import static starfederation.datastar.Consts.ELEMENTS_DATALINE_LITERAL;
import static starfederation.datastar.Consts.MODE_DATALINE_LITERAL;
import static starfederation.datastar.Consts.SELECTOR_DATALINE_LITERAL;
import static starfederation.datastar.Consts.USE_VIEW_TRANSITION_DATALINE_LITERAL;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import starfederation.datastar.enums.ElementPatchMode;
import starfederation.datastar.enums.EventType;
import starfederation.datastar.enums.Namespace;

public final class PatchElements extends AbstractDatastarEvent {

    private PatchElements(EventType eventType, List<String> dataLines) {
        super(eventType, dataLines);
    }

    @Override
    public EventType getEventType() {
        return EventType.PatchElements;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder extends AbstractBuilder<PatchElements> {
        private String selector;
        private ElementPatchMode mode = DEFAULT_ELEMENT_PATCH_MODE; // Default
        private boolean useViewTransition = DEFAULT_ELEMENTS_USE_VIEW_TRANSITIONS; // Default
        private Namespace namespace = Namespace.HTML;
        private String rawData;

        private Builder() {
        }

        public Builder selector(String selector) {
            this.selector = selector;
            return this;
        }

        public Builder mode(ElementPatchMode mode) {
            this.mode = mode;
            return this;
        }

        public Builder useViewTransition(boolean useViewTransition) {
            this.useViewTransition = useViewTransition;
            return this;
        }

        public Builder namespace(Namespace namespace) {
            this.namespace = namespace;
            return this;
        }

        public Builder data(String rawData) {
            this.rawData = rawData;
            return this;
        }

        @Override
        public PatchElements build() {
            if (mode != ElementPatchMode.Remove && (rawData == null || rawData.isBlank())) {
                throw new IllegalArgumentException("Data cannot be null or empty");
            }

            var dataLines = new ArrayList<String>();

            // Add selector
            if (selector != null && !selector.isEmpty()) {
                dataLines.add(SELECTOR_DATALINE_LITERAL + selector.trim());
            }

            // Add mergeMode if not default
            if (mode != DEFAULT_ELEMENT_PATCH_MODE) {
                dataLines.add(MODE_DATALINE_LITERAL + mode);
            }

            // Add useViewTransition if true
            if (useViewTransition != DEFAULT_ELEMENTS_USE_VIEW_TRANSITIONS) {
                dataLines.add(USE_VIEW_TRANSITION_DATALINE_LITERAL + useViewTransition);
            }

            // Add namespace
            if (namespace != Namespace.HTML) {
                var value = switch (namespace) {
                    case HTML -> "html";
                    case SVG -> "svg";
                    case MATHML -> "mathml";
                };
                dataLines.add("namespace " + value);
            }

            // Add raw data as fragments
            if (rawData!= null)
                rawData.lines()
                        .filter(Predicate.not(String::isBlank))
                        .map(line -> ELEMENTS_DATALINE_LITERAL + line)
                        .forEach(dataLines::add);

            return new PatchElements(EventType.PatchElements, dataLines);
        }
    }
}
