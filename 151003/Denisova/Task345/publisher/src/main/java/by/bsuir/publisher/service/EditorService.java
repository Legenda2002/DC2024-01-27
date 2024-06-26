package by.bsuir.publisher.service;

import by.bsuir.publisher.dao.EditorRepository;
import by.bsuir.publisher.model.entity.Editor;
import by.bsuir.publisher.model.request.EditorRequestTo;
import by.bsuir.publisher.model.response.EditorResponseTo;
import by.bsuir.publisher.service.exceptions.ResourceNotFoundException;
import by.bsuir.publisher.service.mapper.EditorMapper;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Data
@RequiredArgsConstructor
@CacheConfig(cacheNames = "editorCache") //использует некоторые общие настройки, связанные с кэшем, на уровне класса
public class EditorService implements RestService<EditorRequestTo, EditorResponseTo> {
    private final EditorRepository editorRepository;

    private final EditorMapper editorMapper;
    private final LogWaitSomeTime logWaitSomeTime;

    @Cacheable(cacheNames = "editors") //запускает заполнение кэша
    @Override
    public List<EditorResponseTo> findAll() {
        logWaitSomeTime.WaitSomeTime(); // лог задержка
        return editorMapper.getListResponseTo(editorRepository.findAll());
    }

    @Cacheable(cacheNames = "editor", key = "#id", unless = "#result == null") //запускает заполнение кэша
    @Override
    public EditorResponseTo findById(Long id) {
        logWaitSomeTime.WaitSomeTime(); // лог задержка
        return editorMapper.getResponseTo(editorRepository
                .findById(id)
                .orElseThrow(() -> editorNotFoundException(id)));
    }

    @CacheEvict(cacheNames = "editors", allEntries = true) //запускает удаление кэша
    @Override
    public EditorResponseTo create(EditorRequestTo editorTo) {
        return editorMapper.getResponseTo(editorRepository.save(editorMapper.getEditor(editorTo)));
    }

    @CacheEvict(cacheNames = "editors", allEntries = true) //запускает удаление кэша
    @Override
    public EditorResponseTo update(EditorRequestTo editorTo) {
        editorRepository
                .findById(editorMapper.getEditor(editorTo).getId())
                .orElseThrow(() -> editorNotFoundException(editorMapper.getEditor(editorTo).getId()));
        return editorMapper.getResponseTo(editorRepository.save(editorMapper.getEditor(editorTo)));
    }

    //перегруппирует несколько операций кэша, которые будут применены к методу
    @Caching(evict = { @CacheEvict(cacheNames = "editor", key = "#id"),
                       @CacheEvict(cacheNames = "editors", allEntries = true) })
    @Override
    public void removeById(Long id) {
        Editor editor = editorRepository
                .findById(id)
                .orElseThrow(() -> editorNotFoundException(id));
        editorRepository.delete(editor);
    }

    private static ResourceNotFoundException editorNotFoundException(Long id) {
        return new ResourceNotFoundException("Failed to find editor with id = " + id, HttpStatus.NOT_FOUND.value() * 100 + 23);
    }
}
