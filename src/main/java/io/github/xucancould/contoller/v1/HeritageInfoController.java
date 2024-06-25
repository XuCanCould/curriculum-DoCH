package io.github.xucancould.contoller.v1;

import io.github.talelin.autoconfigure.exception.NotFoundException;
import io.github.talelin.core.annotation.GroupRequired;
import io.github.talelin.core.annotation.PermissionMeta;
import io.github.xucancould.dto.heritage.CreateOrUpdateCHIDTO;
import io.github.xucancould.model.HeritageInfoDO;
import io.github.xucancould.service.HeritageInfoService;
import io.github.xucancould.vo.CreatedVo;
import io.github.xucancould.vo.UpdatedVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;
import java.util.List;

/**
 * created by Xu on 2024/6/14 10:29.
 */
@RestController
@RequestMapping("/v1/heritageInfo")
public class HeritageInfoController {

    @Autowired
    private HeritageInfoService heritageInfoService;

//    @Autowired
//    private BookService bookService;


    @GetMapping
    public List<HeritageInfoDO> getBooks() {
        return heritageInfoService.findAll();
    }

    @PostMapping
    public CreatedVo createHeritageRecord(@RequestBody CreateOrUpdateCHIDTO heritageInfo) {
        heritageInfoService.createHeritageRecord(heritageInfo);
        return new CreatedVo(16);
    }

    @GetMapping("/{id}")
    public HeritageInfoDO getCultureHeritageInfo(@PathVariable(value = "id") Integer id) {
        HeritageInfoDO heritageInfoDO = heritageInfoService.getById(id);
        return heritageInfoDO;
    }

    // todo
//    @GetMapping("/search")
//    public List<BookDO> searchBook(@RequestParam(value = "title", required = false, defaultValue = "") String title) {
//        return bookService.getBookByKeyword(title);
//    }

    @PutMapping("/{id}")
    public UpdatedVO updateHeritageInfo(@PathVariable("id") @Positive(message = "{id.positive}") Integer id,
                                @RequestBody @Validated CreateOrUpdateCHIDTO validator) {
        HeritageInfoDO heritageInfo = heritageInfoService.getById(id);
        if (heritageInfo == null) {
            throw new NotFoundException(12301);
        }
        heritageInfoService.updateCultureHeritageInfo(heritageInfo, validator);
        return new UpdatedVO(17);
    }

    @DeleteMapping("/{id}")
    @GroupRequired
    @PermissionMeta(value = "删除图书", module = "图书")
    public UpdatedVO deleteHeritageRecord(@PathVariable("id") @Positive(message = "{id.positive}") Integer id) {
        HeritageInfoDO heritageInfo = heritageInfoService.getById(id);
        if (heritageInfo == null) {
            throw new NotFoundException(12301);
        }
        heritageInfoService.deleteById(heritageInfo.getId());
        return new UpdatedVO(18);
    }

}
