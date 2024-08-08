package edu.miu.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * <h1>Maharishi International University<br/>Computer Science Department</h1>
 * 
 * <p>Using Spring Data JPA, provides basic CRUD operations (through {@link JpaRepository}) 
 * and search functionality (through {@link JpaSpecificationExecutor}).</p>
 *
 * @author Payman Salek
 * 
 * @version 1.0.0
 * @since 1.0.0
 * 
 */
@NoRepositoryBean
public interface BaseRepository<T, ID> extends JpaRepository<T, ID>, JpaSpecificationExecutor<T> {

}
