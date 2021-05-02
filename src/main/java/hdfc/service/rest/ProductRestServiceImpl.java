package hdfc.service.rest;

import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.foreveross.springboot.dubbo.utils.Payload;
import hdfc.domain.Product;
import hdfc.repository.ProductRepository;
import hdfc.service.rest.api.ProductRestService;

import javax.ws.rs.*;

@Service("productRestService")
@Path("/api/v1/products")
@Consumes({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
public class ProductRestServiceImpl implements ProductRestService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ProductRepository productRepository;

    @GET
    @Path("/")
    public Payload getProductList() {
        return new Payload(productRepository.findAll());
    }

    @GET
    @Path("{id : \\d+}")
    public Payload getProductById(@PathParam("id") Integer id) {
        return new Payload(productRepository.findOne(id));
    }

    @POST
    @Path("/")
    public Payload createProduct(Product product) {
        Product p = productRepository.save(product);
        return new Payload(p);
    }

    @PUT
    @Path("{id : \\d+}")
    public Payload updateProductById(@PathParam("id") Integer id, Product product) {
        return null;
    }

    @DELETE
    @Path("{id : \\d+}")
    public Payload deleteProductById(@PathParam("id") Integer id) {
        productRepository.delete(id);
        return new Payload(id);
    }
}
