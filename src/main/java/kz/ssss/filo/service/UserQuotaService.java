package kz.ssss.filo.service;

import kz.ssss.filo.model.UserQuota;
import kz.ssss.filo.repository.UserQuotaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserQuotaService {

    private final UserQuotaRepository repository;

    private static final long BASE_QUOTA_BYTES = 500L * 1024 * 1024;

    @Transactional(readOnly = true)
    public UserQuota getUserQuota(long userId) {
        return repository.findById(userId).get();
    }

    @Transactional(readOnly = true)
    public boolean willExceedQuota(long userId, long size) {
        UserQuota user = repository.findById(userId).get();
        long updatedQuota = user.getUsedBytes() + size;
        if (updatedQuota > user.getQuotaBytes()) {
            return true;
        }
        return false;
    }

    @Transactional
    public void initializeQuota(Long id) {
        UserQuota userQuota = new UserQuota(id, 0, BASE_QUOTA_BYTES);
        repository.save(userQuota);
    }

    @Transactional
    public void changeUsedSpaceOnUpload(long userId, long size) {
        UserQuota user = repository.findById(userId).get();
        user.setUsedBytes(user.getUsedBytes() + size);
        repository.save(user);
    }

    @Transactional
    public void changeUsedSpaceOnDelete(long userId, long size) {
        UserQuota user = repository.findById(userId).get();
        user.setUsedBytes(user.getUsedBytes() - size);
        repository.save(user);
    }

}
