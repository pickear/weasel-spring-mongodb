package com.weasel.mongodb;

import java.util.Collections;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import com.google.common.collect.Lists;
import com.weasel.core.helper.DemonPredict;

/**
 * @author Dylan
 * @time 2013-5-16 上午9:43:08
 */
public class Page<T> implements Pageable {

	public final static Direction ASC = Direction.ASC;
	public final static Direction DESC = Direction.DESC;
	
	/**
	 * witch page we now hold
	 */
	private int pageNumber = 0;

	/**
	 * how many records per page
	 */
	private int pageSize = 20;

	/**
	 * 
	 */
	private Sort sort; // for sort

	/**
	 * how many records we can find from database
	 */
	private long total;

	/**
	 * the result we get
	 */
	private List<T> result = Lists.newArrayList();
	
	public Page(){
		this(0,20);
	}
	public Page(int page, int size) {
		this(page, size, null);
	}
	public Page(int page, int size, Sort sort) {
		if (page < 0)
			throw new RuntimeException("page must more than or equal 0");
		if (size <= 0)
			throw new RuntimeException("size must more than 0");
		this.pageNumber = page;
		this.pageSize = size;
		this.sort = sort;
	}

	public Page(Direction direction, String... properties) {
		this(0, 20, direction,properties);
	}
	
	public Page(int page, int size, Direction direction, String... properties) {
		this(page, size, new Sort(direction, properties));
	}

	/**
	 * @return
	 */
	public int getTotalPage() {
		return (int) Math.ceil((double) total / (double) pageSize);
	}

	/**
	 * @return
	 */
	public boolean hasNexPage() {
		return (getPageNumber() + 1) * getPageSize() < total;
	}

	/**
	 * @return
	 */
	public boolean hasPrePage() {
		return getPageNumber() > 0;
	}

	/**
	 * @return
	 */
	public boolean isLastPage() {
		return !hasNexPage();
	}

	/**
	 * d
	 * @return
	 */
	public boolean isFirstPage() {
		return !hasPrePage();
	}
	
	public Page<T> nextPage(){
		if(hasNexPage()){
			++pageNumber;
		}
		return this;
	}
	public Page<T> prePage(){
		if(hasPrePage()){
			--pageNumber;
		}
		return this;
	}

	/**
	 * @return
	 */
	public long getTotal() {
		return total;
	}

	/**
	 * @param total
	 */
	public Page<T> setTotal(long total) {
		this.total = total;
		return this;
	}

	/**
	 * @return
	 */
	public List<T> getResult() {
		return Collections.unmodifiableList(result);
	}

	/**
	 * @param result
	 */
	public Page<T> setResult(List<T> _result) {
		DemonPredict.notNull(result, "result must not be null");
		result = _result;
		return this;
	}

	/**
	 *(non-Javadoc)
	 * @see @see org.springframework.data.domain.Pageable#getPageNumber()
	 */
	@Override
	public int getPageNumber() {
		return pageNumber;
	}

	/**
	 *(non-Javadoc)
	 * @see @see org.springframework.data.domain.Pageable#getPageSize()
	 */
	@Override
	public int getPageSize() {
		return pageSize;
	}

	/**
	 *(non-Javadoc)
	 * @see @see org.springframework.data.domain.Pageable#getOffset()
	 */
	@Override
	public int getOffset() {
		return pageNumber * pageSize;
	}

	/**
	 *(non-Javadoc)
	 * @see @see org.springframework.data.domain.Pageable#getSort()
	 */
	@Override
	public Sort getSort() {

		return sort;
	}

}
