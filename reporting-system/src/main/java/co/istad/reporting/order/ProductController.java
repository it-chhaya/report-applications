package co.istad.reporting.order;

import co.istad.reporting.domain.oracle.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductRepository productRepository;

    @GetMapping
    Page<Product> findAll(
            @RequestParam(required = false, defaultValue = "0") int pageNumber,
            @RequestParam(required = false, defaultValue = "25") int pageSize
    ) {

        // Create pagination request
        // limit, pageNo, sort
        Sort sort = Sort.by(Sort.Direction.ASC, "productName");
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, sort);

        return productRepository.findAll(pageRequest);
    }

}
