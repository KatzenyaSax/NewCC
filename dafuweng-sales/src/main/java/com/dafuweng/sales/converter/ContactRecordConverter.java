package com.dafuweng.sales.converter;

import com.dafuweng.common.core.converter.EntityConverter;
import com.dafuweng.sales.domain.dto.ContactRecordCreateRequest;
import com.dafuweng.sales.domain.vo.ContactRecordVO;
import com.dafuweng.sales.entity.ContactRecord;
import com.dafuweng.sales.enums.ContactType;
import com.dafuweng.sales.enums.IntentionLevel;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ContactRecordConverter extends EntityConverter<ContactRecord, ContactRecordVO> {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "salesRepId", ignore = true)
    @Mapping(target = "intentionAfter", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    ContactRecord toEntity(ContactRecordCreateRequest request);

    @Named("contactTypeDesc")
    default String contactTypeDesc(Integer contactType) {
        if (contactType == null) return null;
        ContactType type = ContactType.fromCode(contactType);
        return type != null ? type.getDesc() : null;
    }

    @Named("intentionBeforeDesc")
    default String intentionBeforeDesc(Integer intentionBefore) {
        if (intentionBefore == null) return null;
        IntentionLevel level = IntentionLevel.fromCode(intentionBefore);
        return level != null ? level.getDesc() : null;
    }

    @Named("intentionAfterDesc")
    default String intentionAfterDesc(Integer intentionAfter) {
        if (intentionAfter == null) return null;
        IntentionLevel level = IntentionLevel.fromCode(intentionAfter);
        return level != null ? level.getDesc() : null;
    }
}
