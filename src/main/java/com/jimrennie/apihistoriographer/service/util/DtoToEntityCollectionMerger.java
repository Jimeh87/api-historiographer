package com.jimrennie.apihistoriographer.service.util;

import lombok.Value;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

import static java.util.stream.Collectors.toSet;

@Value(staticConstructor = "of")
public class DtoToEntityCollectionMerger<T, U> implements BiConsumer<List<T>, Set<U>> {

	private final BiFunction<T, U, Boolean> equalityFn;
	private final Function<T, U> createFn;
	private final BiConsumer<T, U> mergeFn;

	public void accept(List<T> dtos, Set<U> entities) {
		Set<U> removedEntities = entities
				.stream()
				.filter(entity -> findMatch(entity, dtos).isEmpty())
				.collect(toSet());

		Set<U> addedEntities = dtos
				.stream()
				.filter(dto -> entities.stream().noneMatch(entity -> equalityFn.apply(dto, entity)))
				.map(this::create)
				.collect(toSet());

		entities
				.stream()
				.map(entity -> Pair.of(entity, findMatch(entity, dtos)))
				.filter(pair -> pair.getRight().isPresent())
				.forEach(pair -> merge(pair.getRight().get(), pair.getLeft()));


		entities.removeAll(removedEntities);
		entities.addAll(addedEntities);
	}

	private U create(T dto) {
		return merge(dto, createFn.apply(dto));
	}

	private U merge(T dto, U entity) {
		mergeFn.accept(dto, entity);
		return entity;
	}

	private Optional<T> findMatch(U entity, List<T> dtos) {
		return dtos.stream().filter(dto -> equalityFn.apply(dto, entity)).findAny();
	}

}
