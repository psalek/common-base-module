package edu.miu.common.service.mapper;

import java.io.Serializable;

import org.apache.commons.lang3.SerializationUtils;
import org.springframework.stereotype.Component;

import ma.glasnost.orika.BoundMapperFacade;
import ma.glasnost.orika.MapperFactory;

/**
 * <h1>Maharishi International University<br/>
 * Computer Science Department</h1>
 * 
 * <p>
 * This base class configures the Orika mapping framework for mapping different
 * types. By default, it maps all the attributes of the source class that have
 * the same name as the target class and leaves everything else untouched. But
 * this default behavior can be modified by overriding the registerMapper(...)
 * method.
 * </p>
 *
 * @author Payman Salek
 * 
 * @version 1.0.0
 * @since 1.0.0
 * 
 */
@Component
public abstract class BaseMapper<Source, Target extends Serializable> {

	private BoundMapperFacade<Source, Target> mapper;

	protected BaseMapper() {
	}

	protected BaseMapper(MapperFactory mapperFactory, Class<Source> sourceType, Class<Target> targetType) {
		super();

		mapperFactory.classMap(sourceType, targetType)
			.byDefault() // copies all the fields that have the same name and ignores the rest
			.register();

		mapper = mapperFactory.getMapperFacade(sourceType, targetType);

	}

	protected void setMapper(BoundMapperFacade<Source, Target> mapper) {
		this.mapper = mapper;
	}

	public boolean isUpdate(Source source, Target target) {
		if (null == source) {
			return false;
		}

		Target clone = (Target) SerializationUtils.clone(target);

		return mapper.map(source, clone).equals(target) ? false : true;
	}

	public Target map(Source source) {
		return customMapping(source, mapper.map(source));
	}

	public Target map(Source source, Target target) {
		return customMapping(source, mapper.map(source, target));
	}
	
	/**
	 * This method acts as a "hook" method and can be overwritten to customize the mapping of source to target. 
	 * This will be called in addition to the regular mapping defined by the constructor 
	 *
	 * @param source an element of the source system
	 * @param target an element of the target system
	 * 
	 * @return merged target object
	 */
	protected Target customMapping(Source source, Target target) {
		// Default behavior: Basically do nothing
		return target;
	}

}
