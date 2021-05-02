package hdfc.service.rest.api;

import hdfc.domain.Product;
import com.foreveross.springboot.dubbo.utils.Payload;

public interface ProductRestService {

    public Payload getProductList();

    Payload getProductById(Integer id);

    public Payload createProduct(Product product);

    Payload updateProductById(Integer id, Product product);

    Payload deleteProductById(Integer id);
}
