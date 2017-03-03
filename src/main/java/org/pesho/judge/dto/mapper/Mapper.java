package org.pesho.judge.dto.mapper;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;

public abstract class Mapper<E, D> {
	
	public abstract D entityToDTO(E entity);
	public abstract E dtoToEntity(D entity);
	
	/**
	 * If the result class has the same field as the input class the field is copied.
	 * If the result class has the same field concatenated with "Id" or "ID" 
	 * than the result.field = copee.field.id ("id" field is expected).
	 * 
	 * @param copee
	 * @param resultClass
	 * @return
	 */
	public static <A, B> A copySimilarNames(B copee, Class<A> resultClass) {
		try {
			A copy = resultClass.newInstance();
			
			Class<?> sourceClass = copee.getClass();
			
			Field[] sourceFields = sourceClass.getDeclaredFields();
			
			Field[] resultFields = resultClass.getDeclaredFields();
			
			Set<String> resultFieldsSet = new HashSet<>();
			for(Field f : resultFields) resultFieldsSet.add(f.getName());
			
			for (Field field : sourceFields) {
				String name = field.getName();
				
				if (resultFieldsSet.contains(name)) {
					if(!Modifier.isFinal(field.getModifiers())) {
						Field resField = resultClass.getDeclaredField(name);
						
						field.setAccessible(true);
						resField.setAccessible(true);
						
						resField.set(copy, field.get(copee));
					}
				} else if (resultFieldsSet.contains(name + "Id") || resultFieldsSet.contains(name + "ID")) {
					
					String resFieldName = null;
					if(resultFieldsSet.contains(name + "Id")) {
						resFieldName = name + "Id";
					} else {
						resFieldName = name + "ID";
					}
					
					Field resField = resultClass.getDeclaredField(resFieldName);
					
					field.setAccessible(true);
					resField.setAccessible(true);
					
					Object objectField = field.get(copee);
					
					Integer id;
					if(objectField != null) {
						Class<?> objFieldClass = Class.forName(objectField.getClass().getName());
						Field idField = objFieldClass.getDeclaredField("id");
						idField.setAccessible(true);
						id = (Integer)idField.get(objectField);
					} else {
						id = null;
					}
					
					resField.set(copy, id);
				} else if(true) {
					// TODO: Check if source field has suffix "id". If so assing the 
					// proper object ot the result field.
				}
			}
			
			return copy;
		} catch (Exception e) {
			e.printStackTrace();
			throw new IllegalStateException("Exception during mapping objects");
		}
	}
}
