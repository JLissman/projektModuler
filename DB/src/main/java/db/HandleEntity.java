package db;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */




import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

/**
 *
 * @author Jonathan
 */
public class HandleEntity<T> {
    
    public static EntityManagerFactory emf = Persistence.createEntityManagerFactory("PU");
    private Class<T> entityClass;

    public HandleEntity(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    public void create(T entity) {

        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();
        em.persist(entity);
        em.getTransaction().commit();
        
        em.close();
    }

    public void edit(T entity) {

        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();
        em.merge(entity);
        em.getTransaction().commit();
        
        em.close();
    }

    public void remove(T entity) {
        
        EntityManager em = emf.createEntityManager();
        
        em.getTransaction().begin();
        em.remove(em.merge(entity));
        em.getTransaction().commit();

        em.close();
    }

    public T findById(int id) {
        EntityManager em = emf.createEntityManager();

        T x = em.find(entityClass, id);
        
        em.close();
        return x;
    }
    
    
    public List<T> findAllByName(String name){
        EntityManager em = emf.createEntityManager();
        String type = entityClass.getName();
        TypedQuery<T> q = em.createQuery("SELECT X FROM " + type + " x WHERE x.name =:name", entityClass);
        //q.setParameter("type", entityClass.getName()); behöver skapa typ variabeln innan QUERY 
        q.setParameter("name", name);
        
        List<T> ResultList = q.getResultList();
        
        em.close();
        return ResultList;
               
    }
    
    public T findByName(String name){
        EntityManager em = emf.createEntityManager();
        String type = entityClass.getName();
        TypedQuery<T> q = em.createQuery("SELECT X FROM " + type + " x WHERE x.name =:name", entityClass);
        q.setParameter("name", name);
        T x = q.getSingleResult();
        
        em.close();
        return x;
        
    }
            
            
            
    public List<T> getAll(){
        EntityManager em = emf.createEntityManager();
        Query q = em.createQuery("SELECT X FROM " + entityClass.getName() + " x");        
        List<T> ResultList = q.getResultList();
        em.close();
        return ResultList;
    
    }
    public void showAll(){
        EntityManager em = emf.createEntityManager();
        Query q = em.createQuery("SELECT X FROM " + entityClass.getName() + " x");        
        List<T> ResultList = q.getResultList();
        em.close();
        ResultList.forEach(e -> System.out.println(e));    
    }
    
}

