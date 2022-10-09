package com.example.productservice.invoker;

import com.example.productservice.common.InventoryServiceException;
import com.example.productservice.model.Inventory;
import com.example.productservice.model.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import static com.example.productservice.common.ProductServiceConstants.HTTP_STRING;

@Component
public class InventoryServiceInvoker {

    private static final Logger logger = LoggerFactory.getLogger(InventoryServiceInvoker.class);

    @Value("${inventory.service.name}")
    private String inventoryServiceName;

    @Autowired
    private RestTemplate restTemplate;

    public void updateInventory(Inventory inventory) throws Exception {
        String inventoryUrl = HTTP_STRING + inventoryServiceName + "/inventory/update";
        ResponseEntity<Response> response = null;
        try {
            response = restTemplate.postForEntity(inventoryUrl, inventory, Response.class);
            //handle response here
            handleInventoryUpdateResponse(response);
        } catch (Exception e) {
            String error = "error occurred while invoking inventory service" + e.getMessage();
            logger.error(error);
            throw e;
        }

    }

    private Response handleInventoryUpdateResponse(ResponseEntity<Response> response) throws InventoryServiceException {
        Response res = null;
        if (response != null) {
            if (response.hasBody()) {
                int status = response.getBody().getStatus();
                res = prepareResponseForInventoryUpdate(status);
            }
        } else {
            res = prepareResponseForInventoryUpdate(500);
        }
        return res;
    }

    private Response prepareResponseForInventoryUpdate(int status) throws InventoryServiceException {
        String msg = null;
        Response response = null;
        switch (status) {
            case 200:
                msg = "Inventory updated successfully";
                response = new Response(status, msg);
                break;
            case 400:
            case 500:
                msg = "inventory update failed ";
                throw new InventoryServiceException(msg);
        }
        return response;
    }
}
