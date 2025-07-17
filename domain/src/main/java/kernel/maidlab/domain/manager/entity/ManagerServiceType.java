package kernel.maidlab.domain.manager.entity;

import jakarta.persistence.*;
import kernel.maidlab.common.entity.Base;
import kernel.maidlab.domain.manager.enums.ServiceType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "manager_service")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ManagerServiceType extends Base {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id", nullable = false)
    private Manager manager;

    @Column(name = "service_type", nullable = false)
    private ServiceType serviceType;

    private ManagerServiceType(Manager manager, ServiceType serviceType) {
        this.manager = manager;
        this.serviceType = serviceType;
    }

    public static ManagerServiceType managerServiceType(Manager manager, ServiceType serviceType) {
        return new ManagerServiceType(manager, serviceType);
    }

}
