package jpastudy.jpashop.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import jpastudy.jpashop.domain.Order;
import jpastudy.jpashop.domain.OrderStatus;
import jpastudy.jpashop.domain.dto.OrderSearch;
import lombok.RequiredArgsConstructor;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jpastudy.jpashop.domain.QMember;
import jpastudy.jpashop.domain.QOrder;

@Repository
@RequiredArgsConstructor
public class OrderRepository {
	private final EntityManager em;

	public void save(Order order) {
		em.persist(order);
	}

	public Order findOne(Long id) {
		return em.find(Order.class, id);
	}

	public List<Order> findAll(OrderSearch orderSearch) {
		JPAQueryFactory query = new JPAQueryFactory(em);
		QOrder order = QOrder.order;
		QMember member = QMember.member;
		return query
		.select(order)
		.from(order)
		.join(order.member, member)
		.where(statusEq(orderSearch.getOrderStatus()),
			nameLike(orderSearch.getMemberName()))
		.limit(1000)
		.fetch();
	}

	private BooleanExpression statusEq(OrderStatus statusCond) {
		if (statusCond == null) {
			return null;
		}
		return QOrder.order.status.eq(statusCond);
	}

	private BooleanExpression nameLike(String nameCond) {
		if (!StringUtils.hasText(nameCond)) {
			return null;
		}
		return QMember.member.name.like(nameCond);
	}

	public List<Order> findAllJPQL(OrderSearch orderSearch) {
		String jpql = "select o From Order o join o.member m";
		boolean isFirstCondition = true;
		// 주문 상태 검색
		if (orderSearch.getOrderStatus() != null) {
			if (isFirstCondition) {
				jpql += " where";
				isFirstCondition = false;
			} else {
				jpql += " and";
			}
			jpql += " o.status = :status";
		}
		// 회원 이름 검색
		if (StringUtils.hasText(orderSearch.getMemberName())) {
			if (isFirstCondition) {
				jpql += " where";
				isFirstCondition = false;
			} else {
				jpql += " and";
			}
			jpql += " m.name like :name";
		}

		TypedQuery<Order> query = em.createQuery(jpql, Order.class).setMaxResults(1000);
		if (orderSearch.getOrderStatus() != null) {
			query = query.setParameter("status", orderSearch.getOrderStatus());
		}
		if (StringUtils.hasText(orderSearch.getMemberName())) {
			query = query.setParameter("name", orderSearch.getMemberName());
		}
		return query.getResultList();

	}

	public List<Order> findAllByString(OrderSearch orderSearch) {
		String jpql = "select o From Order o join o.member m";
		boolean isFirstCondition = true;
//주문 상태 검색
		if (orderSearch.getOrderStatus() != null) {
			if (isFirstCondition) {
				jpql += " where";
				isFirstCondition = false;
			} else { jpql += " and"; }
			jpql += " o.status = :status";
		}
//회원 이름 검색
		if (StringUtils.hasText(orderSearch.getMemberName())) {
			if (isFirstCondition) {
				jpql += " where";
				isFirstCondition = false;
			} else { jpql += " and"; }
			jpql += " m.name like :name";
		}
		TypedQuery<Order> query = em.createQuery(jpql, Order.class).setMaxResults(1000);
		if (orderSearch.getOrderStatus() != null) {
			query = query.setParameter("status", orderSearch.getOrderStatus());
		}
		if (StringUtils.hasText(orderSearch.getMemberName())) {
			query = query.setParameter("name", orderSearch.getMemberName());
		}
		return query.getResultList();
	}

}
