package org.pesho.judge.dto.mapper;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class Mapper {
	
	@PersistenceContext(unitName = "judge")
	EntityManager em;
	
	public <A, B> List<A> mapList(List<B> copees, Class<A> resultClass) {
		return copees
				.stream()
				.map((x)->map(x, resultClass))
				.collect(Collectors.toList());
	}
	
	/**
	 * If the result class has the same field as the input class the field is copied.
	 * If the result class has the same field concatenated with "Id" or "ID" 
	 * than the result.field = copee.field.id ("id" field is expected).
	 * 
	 * @param copee
	 * @param resultClass
	 * @return
	 */
	public <A, B> A map(B copee, Class<A> resultClass) {
		try {
			if(copee == null) {
				return resultClass.newInstance();
			}
			
			A copy = resultClass.newInstance();
			
			Class<?> sourceClass = copee.getClass();
			
			Field[] sourceFields = sourceClass.getDeclaredFields();
			
			Field[] resultFields = resultClass.getDeclaredFields();
			
			Set<String> resultFieldsSet = new HashSet<>();
			for(Field f : resultFields) resultFieldsSet.add(f.getName());
			
			for (Field field : sourceFields) {
				String name = field.getName();
				
				if (handleSameNameFields(copy, copee, field, resultFieldsSet)) ;
				else if(handleObjectToId(copy, copee, field, resultFieldsSet)) ;
				else if(handleIdToObject(copy, copee, field, resultFieldsSet)) ;
			}
			
			return copy;
		} catch (Exception e) {
			e.printStackTrace();
			throw new IllegalStateException("Exception during mapping objects");
		}
	}
	
	private <A, B> boolean handleSameNameFields(
			A copy, B copee, Field currField, Set<String> resultFieldsSet) {
		
		Class<?> resultClass = copy.getClass();
		String name = currField.getName();
		
		if (resultFieldsSet.contains(name)) {
			if(!Modifier.isFinal(currField.getModifiers())) {
				Field resField;
				try {
					resField = resultClass.getDeclaredField(name);
				} catch (NoSuchFieldException | SecurityException e) {
					e.printStackTrace();
					return false;
				}
				
				currField.setAccessible(true);
				resField.setAccessible(true);
				
				try {
					resField.set(copy, currField.get(copee));
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}
			return true;
		}
		return false;
	}
	
	private <A, B> boolean handleObjectToId(
			A copy, B copee, Field currField, Set<String> resultFieldsSet) {
		
		Class<?> resultClass = copy.getClass();
		String name = currField.getName();
		
		if (resultFieldsSet.contains(name + "Id") || resultFieldsSet.contains(name + "ID")) {
			String resFieldName = null;
			if(resultFieldsSet.contains(name + "Id")) {
				resFieldName = name + "Id";
			} else {
				resFieldName = name + "ID";
			}
			
			try {
				Field resField = resultClass.getDeclaredField(resFieldName);
				
				currField.setAccessible(true);
				resField.setAccessible(true);
				
				Object objectField = currField.get(copee);
				
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
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			return true;
		}
		
		return false;
	}
	
	private <A, B> boolean handleIdToObject(
			A copy, B copee, Field currField, Set<String> resultFieldsSet) {
		
		Class<?> resultClass = copy.getClass();
		String name = currField.getName();
		
		if(name.endsWith("Id") || name.endsWith("ID")) {
			String resFieldName = name.substring(0, name.length()-2);
			
			if (resultFieldsSet.contains(resFieldName)) {
				try {
					Field resField = resultClass.getDeclaredField(resFieldName);
					
					currField.setAccessible(true);
					resField.setAccessible(true);
					
					Object id = currField.get(copee);
					Object resFieldObj;
					
					if(id != null) {
						Class<?> resFieldType = resField.getType();
						resFieldObj = em.find(resFieldType, id);
					} else {
						resFieldObj = null;
					}
					
					System.out.println("res field obj is " + resFieldObj);
					resField.set(copy, resFieldObj);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return true;
		}
		return false;
	}
}
