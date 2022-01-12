/*
 * Copyright 2002-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.customers.web;

import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.samples.petclinic.customers.model.Owner;
import org.springframework.samples.petclinic.customers.model.OwnerRepository;
import org.springframework.web.bind.annotation.*;
import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.Timer;
import io.micrometer.core.instrument.Metrics;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * @author Juergen Hoeller
 * @author Ken Krebs
 * @author Arjen Poutsma
 * @author Michael Isvy
 * @author Maciej Szarlinski
 */
@RequestMapping("/owners")
@RestController
//@Timed("petclinic.owner")
@RequiredArgsConstructor
@Slf4j
class OwnerResource {

    private final OwnerRepository ownerRepository;

    private final Timer totalMethodTimer = Timer.builder("petclinic.owner.request.total")
        .publishPercentiles(0.5, 0.9, 0.99)
        .percentilePrecision(0)
        .distributionStatisticExpiry(Duration.ofMinutes(1))
        .distributionStatisticBufferLength(32767)
        .publishPercentileHistogram()
        .register(Metrics.globalRegistry);

    /**
     * Create Owner
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Owner createOwner(@Valid @RequestBody Owner owner) {
        Timer.Sample methodTimer = Timer.start(Metrics.globalRegistry);
        Owner localOwner = ownerRepository.save(owner);
        // Export the metric
        methodTimer.stop(totalMethodTimer);
        return localOwner;
    }

    /**
     * Read single Owner
     */
    @GetMapping(value = "/{ownerId}")
    public Optional<Owner> findOwner(@PathVariable("ownerId") int ownerId) {
        Timer.Sample methodTimer = Timer.start(Metrics.globalRegistry);
        Optional<Owner> optionalOwner = ownerRepository.findById(ownerId);
        // Export the metric
        methodTimer.stop(totalMethodTimer);
        return optionalOwner;
    }

    /**
     * Read List of Owners
     */
    @GetMapping
    public List<Owner> findAll() {
        Timer.Sample methodTimer = Timer.start(Metrics.globalRegistry);
        List<Owner> listOwner = ownerRepository.findAll();
        // Export the metric
        methodTimer.stop(totalMethodTimer);
        return listOwner;
    }

    /**
     * Update Owner
     */
    @PutMapping(value = "/{ownerId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateOwner(@PathVariable("ownerId") int ownerId, @Valid @RequestBody Owner ownerRequest) {
        Timer.Sample methodTimer = Timer.start(Metrics.globalRegistry);
        final Optional<Owner> owner = ownerRepository.findById(ownerId);
        final Owner ownerModel = owner.orElseThrow(() -> new ResourceNotFoundException("Owner "+ownerId+" not found"));
        // This is done by hand for simplicity purpose. In a real life use-case we should consider using MapStruct.
        ownerModel.setFirstName(ownerRequest.getFirstName());
        ownerModel.setLastName(ownerRequest.getLastName());
        ownerModel.setCity(ownerRequest.getCity());
        ownerModel.setAddress(ownerRequest.getAddress());
        ownerModel.setTelephone(ownerRequest.getTelephone());
        log.info("Saving owner {}", ownerModel);
        ownerRepository.save(ownerModel);
        // Export the metric
        methodTimer.stop(totalMethodTimer);
    }
}
