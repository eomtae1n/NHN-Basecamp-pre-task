package org.zerock.ex2.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.TestExecutionListeners;
import org.zerock.ex2.entity.Memo;

import javax.print.attribute.standard.PageRanges;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class MemoRepositoryTests {

    @Autowired
    MemoRepository memoRepository;

    @Test
    public void testClass() {
        System.out.println(memoRepository.getClass().getName());
    }

    @Test
    public void testInsertDummies() {

        IntStream.rangeClosed(1, 100).forEach(i -> {
            Memo memo = Memo.builder().memoText("Sample..."+i).build();
            memoRepository.save(memo);
        });
    }

    @Test
    public void testSelect() {
        //데이터베이스에 존재하는 mno
        Long mno = 99L;

        Optional<Memo> result = memoRepository.findById(mno);

        System.out.println("====================================");

        if(result.isPresent()) {
            Memo memo = result.get();
            System.out.println(memo);
        }
    }

    @Transactional
    @Test
    public void testSelect2() {
        //데이터베이스에 존재하는 mno
        Long mno = 100L;

        Memo memo = memoRepository.getOne(mno);

        System.out.println("====================================");

        System.out.println(memo);
    }

    @Test
    public void testUpdate() {

        Long mno = 100L;

        Memo memo = Memo.builder().mno(mno).memoText("Update Text..." + mno).build();

        System.out.println(memoRepository.save(memo));
    }

    @Test
    public void testDelete() {
        Long mno = 100L;

        memoRepository.deleteById(mno);
    }

    @Test
    public void testPageDefault() {
        //1페이지 10개
        Pageable pageable = PageRequest.of(0, 10);

        Page<Memo> result = memoRepository.findAll(pageable);

        System.out.println(result);

        System.out.println("====================================");

        System.out.println("Total Pages: " + result.getTotalPages()); // 총 페이지 수

        System.out.println("Total Count: " + result.getTotalElements()); // 전체 개수

        System.out.println("Page Number: " + result.getNumber()); // 현재 페이지 번호 0부터 시작

        System.out.println("Page Size: " + result.getSize()); //페이지당 데이터 개수

        System.out.println("has next page?: " + result.hasNext()); // 다음 페이지 존재 여부

        System.out.println("first page?: " + result.isFirst()); // 시작 페이지(0) 여부
        System.out.println("====================================");

        for (Memo memo : result.getContent()) {
            System.out.println(memo);
        }
    }

    @Test
    public void testSort() {
        Sort sort1 = Sort.by("mno").descending();
        Sort sort2 = Sort.by("memoText").ascending();
        Sort sortAll = sort1.and(sort2);

        Pageable pageable = PageRequest.of(0, 10, sortAll);

        Page<Memo> result = memoRepository.findAll(pageable);

        result.get().forEach(memo -> {
            System.out.println(memo);
        });
    }

    @Test
    public void testQueryMethods() {

        List<Memo> list = memoRepository.findByMnoBetweenOrderByMnoDesc(70L, 80L);

        for (Memo memo : list) {
            System.out.println(memo);
        }
    }

    @Test
    public void testQueryMethodWithPagable() {

        Pageable pageable  = PageRequest.of(0, 10, Sort.by("mno").descending());

        Page<Memo> result = memoRepository.findByMnoBetween(10L, 50L, pageable);

        result.get().forEach(memo -> System.out.println(memo));

    }

    @Commit
    @Transactional
    @Test
    public void testDeleteQueryMethods() {
        memoRepository.deleteMemoByMnoLessThan(10L);
    }

    @Test
    public void testUpdateMemoTextWithQuery() {
        Long mno = 50L;

        Memo memo = Memo.builder().mno(mno).memoText("New Text..." + mno).build();

        memoRepository.updateMemoText(memo);
    }

    @Test
    public void testGetListWithQuery() {

        Pageable pageable = PageRequest.of(0, 10);

        Long mno = 50L;

        Page<Memo> result = memoRepository.getListWithQuery(mno, pageable);

        result.get().forEach(memo -> System.out.println(memo));
    }

    @Test
    public void testGetListWithQueryObject() {
        Long mno = 40L;

        Pageable pageable = PageRequest.of(0, 10);

        Page<Object[]> result = memoRepository.getListWithQueryObject(mno, pageable);

        result.get().forEach(objects ->
        {
            for (Object object:objects) {
                System.out.print(object + " ");
            }
            System.out.println();
        });
    }

    @Test
    public void testGetNativeResult() {
        List<Object[]> result = memoRepository.getNativeResult();

        for(Object[] object : result) {
            for(Object o : object) {
                System.out.print(o + " ");
            }
            System.out.println();
        }
    }
}
