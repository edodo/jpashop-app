package jpastudy.jpashop.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import jpastudy.jpashop.domain.Member;

@Repository
public class MemberRepository {
	@PersistenceContext
	private EntityManager em;

	public void save(Member member) {
		em.persist(member);
	}

	public Member findOne(Long id) {
		return em.find(Member.class, id);
	}

	public List<Member> findAll() {
		TypedQuery<Member> query = em.createQuery("select m from Member m", Member.class);
		return query.getResultList();
		//return em.createQuery("select m from Member m", Member.class).getResultList();
	}

	public List<Member> findByName(String name) {
		TypedQuery<Member> query = em.createQuery("select m from Member m where m.name = :name", Member.class);
		return query
				.setParameter("name", name)
				.getResultList();

//		return em.createQuery("select m from Member m where m.name = :name", Member.class)
//				.setParameter("name", name)
//				.getResultList();
	}
}
