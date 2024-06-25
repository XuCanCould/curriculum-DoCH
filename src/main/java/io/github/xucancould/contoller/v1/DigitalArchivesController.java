package io.github.xucancould.contoller.v1;

import io.github.talelin.autoconfigure.exception.NotFoundException;
import io.github.talelin.core.annotation.GroupRequired;
import io.github.talelin.core.annotation.LoginRequired;
import io.github.talelin.core.annotation.PermissionMeta;
import io.github.xucancould.dto.book.CreateOrUpdateBookDTO;
import io.github.xucancould.dto.digital.CreateOrUpdateCHDADTO;
import io.github.xucancould.model.BookDO;
import io.github.xucancould.model.DigitalArchivesDO;
import io.github.xucancould.service.BookService;
import io.github.xucancould.service.DigitalArchivesService;
import io.github.xucancould.service.HeritageInfoService;
import io.github.xucancould.vo.CreatedVo;
import io.github.xucancould.vo.UpdatedVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;
import java.util.List;

/**
 * created by Xu on 2024/6/17 10:00.
 */
@RestController
@RequestMapping("/v1/digitalArchives")
public class DigitalArchivesController {
    @Autowired
    private DigitalArchivesService digitalArchivesService;

    @Autowired
    private HeritageInfoService heritageInfoService;


    @LoginRequired
    @GetMapping("/{id}")
    public DigitalArchivesDO getDigitalArchive(@PathVariable(value = "id") Integer id) {
        DigitalArchivesDO digital = digitalArchivesService.getById(id);
        return digital;
    }

    @GetMapping
    @LoginRequired
    public List<DigitalArchivesDO> getDigitalArchives() {
        return digitalArchivesService.findAll();
    }

    @PostMapping
    @LoginRequired
    public CreatedVo createDigitalArchives(@RequestBody CreateOrUpdateCHDADTO digital) {
        digitalArchivesService.createDigitalArchive(digital);
        return new CreatedVo(19);
    }

    @PutMapping("/{id}")
    @LoginRequired
    public UpdatedVO updateDigitalArchive(@PathVariable("id") @Positive(message = "{id.positive}") Integer id,
                                @RequestBody @Validated CreateOrUpdateCHDADTO validator) {
        DigitalArchivesDO archives = digitalArchivesService.getById(id);
        if (archives == null) {
            throw new NotFoundException(12301);
        }
        digitalArchivesService.updateArchives(archives, validator);
        return new UpdatedVO(20);
    }

    @DeleteMapping("/{id}")
    @GroupRequired
    @PermissionMeta(value = "删除文化遗产信息", module = "数字化文化遗产")
    @LoginRequired
    public UpdatedVO deleteDigitalArchive(@PathVariable("id") @Positive(message = "{id.positive}") Integer id) {
        DigitalArchivesDO digitalArchivesDO = digitalArchivesService.getById(id);
        if (digitalArchivesDO == null) {
            throw new NotFoundException(12302);
        }
        digitalArchivesService.deleteById(digitalArchivesDO.getId());
        return new UpdatedVO(21);
    }

//    @GetMapping("/search")
//    public List<BookDO> searchDigitalArchive(@RequestParam(value = "title", required = false, defaultValue = "") String title) {
//        return bookService.getBookByKeyword(title);
//    }

    @GetMapping("/relative/{id}")
    @LoginRequired
    public List<DigitalArchivesDO> getDigitalArchives(@PathVariable("id") @Positive(message = "{id.positive}") Integer id) {
        boolean heritageInfoId = heritageInfoService.checkHeritageInfoExistById(id);
        if (!heritageInfoId) {
            throw new NotFoundException(12301);
        }
        return digitalArchivesService.getArchivesByHeritageId(id);
    }
}
