package com.es.phoneshop.service.implementations;

import com.es.phoneshop.dao.implementations.ArrayListProductDao;
import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.service.RecentlyViewedService;
import jakarta.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.List;

public class DefaultRecentlyViewedService implements RecentlyViewedService {

    private static final String VIEWED_SESSION_ATTRIBUTE = DefaultRecentlyViewedService.class.getName() + ".viewed";

    private static final int MAX_RECENTLY_VIEWED = 3;

    private ProductDao productDao;

    private static DefaultRecentlyViewedService instance;

    public static synchronized DefaultRecentlyViewedService getInstance() {
        if (instance == null) {
            instance = new DefaultRecentlyViewedService();
        }
        return instance;
    }

    private DefaultRecentlyViewedService() {
        productDao = ArrayListProductDao.getInstance();
    }

    @Override
    public List<Product> getRecentlyViewedProducts(HttpSession session) {
        List<Product> recentlyViewedProducts = (List<Product>) session.getAttribute(VIEWED_SESSION_ATTRIBUTE);
        if (recentlyViewedProducts == null) {
            recentlyViewedProducts = new ArrayList<>();
            session.setAttribute(VIEWED_SESSION_ATTRIBUTE, recentlyViewedProducts);
        }
        return recentlyViewedProducts;
    }

    @Override
    public void addRecentlyViewedProduct(HttpSession session, Long productId) {
        List<Product> recentlyViewedProducts = getRecentlyViewedProducts(session);
        Product product = productDao.get(productId);

        synchronized (recentlyViewedProducts) {
            recentlyViewedProducts.stream().
                    filter(viewedProduct -> product.getId().equals(viewedProduct.getId())).
                    findAny().
                    ifPresent(viewedProduct -> recentlyViewedProducts.remove(viewedProduct));

            recentlyViewedProducts.add(0, product);

            if (recentlyViewedProducts.size() > MAX_RECENTLY_VIEWED) {
                recentlyViewedProducts.remove(recentlyViewedProducts.size() - 1);
            }
        }
    }
}
