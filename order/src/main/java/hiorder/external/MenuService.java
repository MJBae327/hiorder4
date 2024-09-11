
package hiorder.external;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Date;
import java.util.List;


@FeignClient(name = "menu", url = "${api.url.menu}")
 
public interface MenuService {
    @GetMapping(path="/menus/search/findByGetMenu/{id}")
    public Menu getMenu(@PathVariableGetMenuQuery getMenuQuery);

}



