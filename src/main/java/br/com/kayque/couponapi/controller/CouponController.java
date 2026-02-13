package br.com.kayque.couponapi.controller;

import br.com.kayque.couponapi.dto.CouponResponse;
import br.com.kayque.couponapi.dto.CreateCouponRequest;
import br.com.kayque.couponapi.domain.model.Coupon;
import br.com.kayque.couponapi.usecase.CreateCouponUseCase;
import br.com.kayque.couponapi.usecase.DeleteCouponUseCase;
import br.com.kayque.couponapi.usecase.GetCouponByIdUseCase;
import br.com.kayque.couponapi.usecase.command.CreateCouponCommand;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/coupon")
@Tag(name = "Coupon")
public class CouponController {

  private final CreateCouponUseCase createCouponUseCase;
  private final GetCouponByIdUseCase getCouponByIdUseCase;
  private final DeleteCouponUseCase deleteCouponUseCase;

  public CouponController(CreateCouponUseCase createCouponUseCase,
                          GetCouponByIdUseCase getCouponByIdUseCase,
                          DeleteCouponUseCase deleteCouponUseCase) {
    this.createCouponUseCase = createCouponUseCase;
    this.getCouponByIdUseCase = getCouponByIdUseCase;
    this.deleteCouponUseCase = deleteCouponUseCase;
  }

  @PostMapping
  @Operation(summary = "Criar um novo cupom")
  public ResponseEntity<CouponResponse> create(@Valid @RequestBody CreateCouponRequest request) {
    boolean published = request.published() != null && request.published();

    Coupon created = createCouponUseCase.execute(new CreateCouponCommand(
        request.code(),
        request.description(),
        request.discountValue(),
        request.expirationDate(),
        published
    ));

    URI location = URI.create("/coupon/" + created.getId());
    return ResponseEntity.created(location).body(CouponResponse.from(created));
  }

  @GetMapping("/{id}")
  @Operation(summary = "Obter cupom por ID")
  public CouponResponse getById(@PathVariable UUID id) {
    return CouponResponse.from(getCouponByIdUseCase.execute(id));
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Excluir um cupom (soft delete)")
  public ResponseEntity<Void> delete(@PathVariable UUID id) {
    deleteCouponUseCase.execute(id);
    return ResponseEntity.noContent().build();
  }
}
