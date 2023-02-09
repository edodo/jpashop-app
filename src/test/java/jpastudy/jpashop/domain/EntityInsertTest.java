package jpastudy.jpashop.domain;

import jpastudy.jpashop.domain.item.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.time.LocalDateTime;

@SpringBootTest
@Transactional
public class EntityInsertTest {
    @Autowired
    EntityManager em;

    @Test
    @Rollback(value = false)
    public void insert() throws Exception {
        // 주문(Order) 생성
        Order order = new Order();

        // 회원(Member) 생성
        Member member = new Member();
        member.setName("부트");

        // Member에 Address를 저장
        Address address = new Address("서울", "11번지", "85120");
        member.setAddress(address);

        em.persist(member);
        // Order에 Member를 저장
        order.setMember(member);
        
        // 배송생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());
        delivery.setStatus(DeliveryStatus.READY);

        // order에 Delivery를 저장
        order.setDelivery(delivery);
        em.persist(order);
        
        //상품생성
        Book book = new Book();
        book.setName("JPA책");
        book.setPrice(10000);
        book.setStockQuantity(100);
        book.setAuthor("김처자");
        book.setIsbn("1234");

        em.persist(book);

        OrderItem orderItem = new OrderItem();
        orderItem.setOrderPrice(10000);
        orderItem.setCount(2);
        // OrderItem에 Item(Book) 저장
        orderItem.setItem(book);

        // Order에 OrderItem 저장
        order.addOrderItem(orderItem);

        // Order에 주문날짜 저장
        order.setOrderDate(LocalDateTime.now());

        // Order에 주문상태 저장
        order.setStatus(OrderStatus.ORDER);

        // Order를 저장
        em.persist(order);
    }
}
