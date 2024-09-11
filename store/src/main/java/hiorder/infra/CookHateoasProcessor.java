package hiorder.infra;

import hiorder.domain.*;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.stereotype.Component;

@Component
public class CookHateoasProcessor
    implements RepresentationModelProcessor<EntityModel<Cook>> {

    @Override
    public EntityModel<Cook> process(EntityModel<Cook> model) {
        model.add(
            Link
                .of(model.getRequiredLink("self").getHref() + "//isaccept")
                .withRel("/isaccept")
        );
        model.add(
            Link
                .of(model.getRequiredLink("self").getHref() + "/start")
                .withRel("start")
        );
        model.add(
            Link
                .of(model.getRequiredLink("self").getHref() + "/finish")
                .withRel("finish")
        );

        return model;
    }
}
