package edu.miu.common.repository.search;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.data.jpa.domain.Specification;

/**
 * <h1>Maharishi International University<br/>Computer Science Department</h1>
 * 
 * <p>Uses pattern matching and regular expressions to extract the list of {@link SearchCriteria} 
 * from the incoming query.</p>
 *
 * @author Payman Salek
 * 
 * @version 1.0.0
 * @since 1.0.0
 * 
 */
public class SpecificationBuilder<T> {
	
	private static final Pattern pattern = Pattern.compile("(\\w+?)(:|<|>)(\\w+?),");
     
    private final List<SearchCriteria> params;
 
    public SpecificationBuilder(String query) {
        params = new ArrayList<>();
        Matcher matcher = pattern.matcher(query + ",");
        while (matcher.find()) {
            this.with(matcher.group(1), matcher.group(2), matcher.group(3));
        }
    }
 
    private SpecificationBuilder<T> with(String key, String operation, Object value) {
        params.add(new SearchCriteria(key, operation, value));
        return this;
    }
 
    public Specification<T> build() {
        if (params.isEmpty()) {
            return null;
        }
 
        List<Specification<T>> specs = params.stream()
        		.map(EntitySpecification<T>::new)
        		.collect(Collectors.toList());
         
        Specification<T> result = specs.get(0);
 
        for (int i = 1; i < specs.size(); i++) {
            result = Specification.where(result).and(specs.get(i));
        }     
        
        return result;
    }

}
